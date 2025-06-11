package BT2.spring_boot.service;

import BT2.spring_boot.modelDto.FlightCardDto;
import BT2.spring_boot.modelDto.mapToFlightCardDtos;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SearchService {

    @Value("${amadeus.host}")
    private String host;

    private final RestTemplate restTemplate;
    private final AmadeusService authService;

    public SearchService(RestTemplate restTemplate, AmadeusService authService) {
        this.restTemplate = restTemplate;
        this.authService = authService;
    }

    public String searchAirports(String keyword) {
        String token = authService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = host + "/v1/reference-data/locations?subType=AIRPORT&keyword=" + keyword + "&page[limit]=5";

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, request, String.class
        );

        return response.getBody();
    }
    public String getAirlineInfo(String airlineCode) {
        String token = authService.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = host + "/v1/reference-data/airlines?airlineCodes=" + airlineCode;
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, request, String.class
        );
        return response.getBody();
    }
    public String searchFlights(String origin, String destination, String date, int adults, String currency, boolean nonStop) {
        String token = authService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = """
        {
          "currencyCode": "%s",
          "originDestinations": [
            {
              "id": "1",
              "originLocationCode": "%s",
              "destinationLocationCode": "%s",
              "departureDateTimeRange": {
                "date": "%s"
              }
            }
          ],
          "travelers": [
            %s
          ],
          "sources": [ "GDS" ],
          "searchCriteria": {
            "maxFlightOffers": 10,
            "flightFilters": {
              "connectionRestriction": {
                "nonStopPreferred": %s
              }
            }
          }
        }
        """.formatted(
                currency,
                origin,
                destination,
                date,
                generateTravelers(adults),
                nonStop
        );

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        String url = host.replace("/v1", "/v2") + "/shopping/flight-offers";

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, request, String.class
        );

        return response.getBody();
    }

    private String generateTravelers(int adults) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= adults; i++) {
            sb.append(String.format("""
            {
              "id": "%d",
              "travelerType": "ADULT"
            }%s
        """, i, (i < adults ? "," : "")));
        }
        return sb.toString();
    }

    private List<FlightCardDto> mapToFlightCardDtos(String rawJson) {
        List<FlightCardDto> cards = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(rawJson);
            JsonNode data = root.get("data");

            if (data != null && data.isArray()) {
                for (JsonNode offer : data) {
                    FlightCardDto dto = new FlightCardDto();
                    dto.setId(offer.get("id").asText());

                    JsonNode itineraries = offer.get("itineraries");
                    JsonNode segments = itineraries.get(0).get("segments");

                    JsonNode firstSegment = segments.get(0);
                    JsonNode lastSegment = segments.get(segments.size() - 1);

                    dto.setDepartureAirport(firstSegment.get("departure").get("iataCode").asText());
                    dto.setArrivalAirport(lastSegment.get("arrival").get("iataCode").asText());
                    dto.setDepartureTime(firstSegment.get("departure").get("at").asText());
                    dto.setArrivalTime(lastSegment.get("arrival").get("at").asText());

                    String carrierCode = firstSegment.get("carrierCode").asText();
                    dto.setAirline(carrierCode); // You can enhance this later using the Airline Info API

                    dto.setTotalDuration(itineraries.get(0).get("duration").asText());

                    JsonNode priceNode = offer.get("price");
                    dto.setPrice(priceNode.get("total").asText() + " " + priceNode.get("currency").asText());

                    cards.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }
    private String buildFlightSearchBody(String origin, String destination, String date, int adults, String currency, boolean nonStop) {
        return """
        {
          "currencyCode": "%s",
          "originDestinations": [
            {
              "id": "1",
              "originLocationCode": "%s",
              "destinationLocationCode": "%s",
              "departureDateTimeRange": {
                "date": "%s"
              }
            }
          ],
          "travelers": [
            %s
          ],
          "sources": [ "GDS" ],
          "searchCriteria": {
            "maxFlightOffers": 10,
            "flightFilters": {
              "connectionRestriction": {
                "nonStopPreferred": %s
              }
            }
          }
        }
        """.formatted(
                currency,
                origin,
                destination,
                date,
                generateTravelers(adults),
                nonStop
        );
    }
    public List<FlightCardDto> searchFlightsSimplified(String origin, String destination, String date, int adults, String currency, boolean nonStop) {
        String token = authService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = buildFlightSearchBody(origin, destination, date, adults, currency, nonStop);
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        String url = host.replace("/v1", "/v2") + "/shopping/flight-offers";

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, request, String.class
        );

        return mapToFlightCardDtos(response.getBody());
    }






}
