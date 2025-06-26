package BT2.spring_boot.service.impl;

import BT2.spring_boot.modelDto.*;
import BT2.spring_boot.service.AmadeusAuthService;
import BT2.spring_boot.service.FlightSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FlightSearchServiceImpl implements FlightSearchService {

    private static final Logger log = LoggerFactory.getLogger(FlightSearchServiceImpl.class);
    private final Map<String, FlightDTO> flightCache = new ConcurrentHashMap<>();
    private final AmadeusAuthService authService;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public FlightSearchServiceImpl(
            AmadeusAuthService authService,
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper
    ) {
        this.authService = authService;
        this.webClient = webClientBuilder.baseUrl("https://test.api.amadeus.com").build();
        this.objectMapper = objectMapper;
    }

    @Override
    public List<FlightOfferResponse> searchFlights(FlightSearchRequestDto request) {
        try {
            validateSearchRequest(convertToRequestModel(request));
            String token = authService.getAccessToken();

            WebClient.RequestHeadersSpec<?> requestSpec = webClient.get()
                    .uri(uriBuilder -> {
                        var builder = uriBuilder
                                .path("/v2/shopping/flight-offers")
                                .queryParam("originLocationCode", request.getOriginLocationCode())
                                .queryParam("destinationLocationCode", request.getDestinationLocationCode())
                                .queryParam("departureDate", request.getDepartureDate())
                                .queryParam("adults", request.getAdults())
                                .queryParam("currencyCode", request.getCurrency())
                                .queryParam("nonStop", request.isNonStop());

                        if (request.getReturnDate() != null && !request.getReturnDate().isEmpty()) {
                            builder.queryParam("returnDate", request.getReturnDate());
                        }

                        return builder.build();
                    })
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            String responseBody = requestSpec
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            FlightOfferResponseWrapper wrapper = objectMapper.readValue(responseBody, FlightOfferResponseWrapper.class);
            return wrapper.getData();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public PaginatedResponse<FlightDTO> searchFlightsWithPagination(FlightSearchRequestDto request, int page, int size) {
        try {
            List<FlightDTO> allResults = maptOFlightDTOs(searchFlights(request), request.getAdults());

            String sortBy = request.getSortBy();
            String sortOrder = request.getSortOrder();

            if (sortBy != null) {
                Comparator<FlightDTO> comparator = null;

                if ("price".equalsIgnoreCase(sortBy)) {
                    comparator = Comparator.comparing(FlightDTO::getTotalPriceValue);
                } else if ("duration".equalsIgnoreCase(sortBy)) {
                    comparator = Comparator.comparingLong(FlightDTO::getTotalDurationInMinutes);
                }

                if (comparator != null) {
                    if ("desc".equalsIgnoreCase(sortOrder)) {
                        comparator = comparator.reversed();
                    }
                    allResults.sort(comparator);
                }

            }

            int totalItems = allResults.size();
            int fromIndex = Math.min(totalItems, page * size);
            int toIndex = Math.min(fromIndex + size, totalItems);
            List<FlightDTO> pageData = fromIndex > totalItems ? Collections.emptyList() : allResults.subList(fromIndex, toIndex);

            return new PaginatedResponse<>(pageData, totalItems, page, size);
        } catch (Exception e) {
            log.error("❌ Error during paginated flight search: ", e);
            return new PaginatedResponse<>(Collections.emptyList(), 0, page, size);
        }
    }

    @Override
    public void validateSearchRequest(FlightSearchRequest request) {
        LocalDate today = LocalDate.now();

        if (request.getDepartureDate() == null || request.getDepartureDate().isBefore(today)) {
            throw new IllegalArgumentException("Departure date cannot be in the past.");
        }

        if (request.getReturnDate() != null && request.getReturnDate().isBefore(request.getDepartureDate())) {
            throw new IllegalArgumentException("Return date cannot be before departure date.");
        }

        if (request.getAdults() <= 0) {
            throw new IllegalArgumentException("At least one adult must be specified.");
        }

        List<String> validCurrencies = List.of("USD", "MXN", "EUR");
        if (!validCurrencies.contains(request.getCurrencyCode())) {
            throw new IllegalArgumentException("Unsupported currency: " + request.getCurrencyCode());
        }
    }

    @Override
    public List<FlightDTO> maptOFlightDTOs(List<FlightOfferResponse> offers, int adultCount) {
        List<FlightDTO> results = new ArrayList<>();

        for (FlightOfferResponse offer : offers) {
            if (offer.getItineraries() == null || offer.getItineraries().isEmpty()) continue;

            ItineraryDto itinerary = offer.getItineraries().get(0);
            List<SegmentDto> segments = itinerary.getSegments();
            if (segments == null || segments.isEmpty()) continue;

            SegmentDto first = segments.get(0);
            SegmentDto last = segments.get(segments.size() - 1);

            FlightDTO dto = new FlightDTO();
            String uuid = UUID.randomUUID().toString();
            dto.setId(uuid);
            flightCache.put(uuid, dto);

            if (offer.getItineraries().size() > 1) {
                ItineraryDto returnItinerary = offer.getItineraries().get(1);
                dto.setReturnSegments(returnItinerary.getSegments());
            }

            dto.setDepartureTime(first.getDeparture().getAt());
            dto.setArrivalTime(last.getArrival().getAt());
            dto.setDepartureAirport(first.getDeparture().getIataCode());
            dto.setArrivalAirport(last.getArrival().getIataCode());
            dto.setDuration(itinerary.getDuration());

            dto.setAirlineCode(first.getCarrierCode());
            dto.setOperatingAirlineCode(first.getOperating() != null ? first.getOperating().getCarrierCode() : null);
            dto.setAirlineName(first.getCarrierCode());
            dto.setOperatingAirlineName(first.getOperating() != null ? first.getOperating().getCarrierCode() : null);
            dto.setSegments(segments);

            List<AmenityDTO> allAmenities = new ArrayList<>();
            for (SegmentDto segment : segments) {
                if (segment.getAmenities() != null) {
                    allAmenities.addAll(segment.getAmenities());
                }
            }
            dto.setAmenities(allAmenities);

            double total = 0;
            double base = 0;
            String currency = null;

            // 🧠 Build map of ALL segments (including return)
            Map<String, SegmentDto> allSegmentsById = new HashMap<>();
            for (ItineraryDto it : offer.getItineraries()) {
                for (SegmentDto seg : it.getSegments()) {
                    allSegmentsById.put(seg.getId(), seg);
                }
            }

            if (offer.getTravelerPricings() != null) {
                for (TravelerPricingDto pricing : offer.getTravelerPricings()) {
                    if ("ADULT".equalsIgnoreCase(pricing.getTravelerType()) && pricing.getPrice() != null) {
                        total += Double.parseDouble(pricing.getPrice().getTotal());
                        base += Double.parseDouble(pricing.getPrice().getBase());
                        if (currency == null) {
                            currency = pricing.getPrice().getCurrency();
                        }
                    }

                    if (pricing.getFareDetailsBySegment() != null) {
                        for (FareDetailsPerSegment detail : pricing.getFareDetailsBySegment()) {
                            SegmentDto segment = allSegmentsById.get(detail.getSegmentId());
                            if (segment != null) {
                                segment.setCabin(detail.getCabin() != null ? detail.getCabin() : "ECONOMY");
                                segment.setFareBasis(detail.getFareBasis());
                                segment.setBrandedFare(detail.getBrandedFare());
                                segment.setTravelClass(detail.getClass_() != null ? detail.getClass_() : "Not specified");

                                if (detail.getIncludedCheckedBags() != null) {
                                    segment.setIncludedCheckedBags(detail.getIncludedCheckedBags().getQuantity());
                                }
                            } else {
                                log.warn("⚠️ Segment [{}] not found in segment map for fare detail.", detail.getSegmentId());
                            }
                        }
                    }
                }
            }

            dto.setTotalPrice(total);
            dto.setBasePrice(base);
            dto.setFees(total - base);
            dto.setPricePerAdult(adultCount > 0 ? total / adultCount : 0);
            dto.setCurrency(currency);
            dto.setLayoverDurations(calculateLayoverDurations(segments));

            results.add(dto);
        }

        return results;
    }
    private List<String> calculateLayoverDurations(List<SegmentDto> segments) {
        List<String> layovers = new ArrayList<>();

        for (int i = 0; i < segments.size() - 1; i++) {
            SegmentDto current = segments.get(i);
            SegmentDto next = segments.get(i + 1);

            try {
                LocalDateTime arrival = LocalDateTime.parse(current.getArrival().getAt());
                LocalDateTime nextDeparture = LocalDateTime.parse(next.getDeparture().getAt());

                Duration layover = Duration.between(arrival, nextDeparture);
                String durationStr = String.format("%dh %02dm", layover.toHours(), layover.toMinutesPart());
                layovers.add("Layover: " + durationStr);
            } catch (Exception e) {
                layovers.add("Layover time unavailable");
            }
        }

        return layovers;
    }

    private FlightSearchRequest convertToRequestModel(FlightSearchRequestDto dto) {
        LocalDate departure = LocalDate.parse(dto.getDepartureDate());
        LocalDate returnDate = dto.getReturnDate() != null && !dto.getReturnDate().isEmpty()
                ? LocalDate.parse(dto.getReturnDate())
                : null;

        return new FlightSearchRequest(
                dto.getOriginLocationCode(),
                dto.getDestinationLocationCode(),
                departure,
                returnDate,
                dto.getAdults(),
                dto.getCurrency(),
                dto.isNonStop()
        );
    }

    @Override
    public FlightDTO getFlightById(String id) {
        return flightCache.get(id);
    }
}