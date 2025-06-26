package BT2.spring_boot.controller;

import BT2.spring_boot.modelDto.FlightSearchRequestDto;
import BT2.spring_boot.modelDto.FlightDTO;
import BT2.spring_boot.modelDto.FlightOfferResponse;
import BT2.spring_boot.modelDto.PaginatedResponse;
import BT2.spring_boot.service.FlightSearchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "*") // You can restrict this in production
public class FlightSearchController {

    private final FlightSearchService flightSearchService;

    public FlightSearchController(FlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }

    /**
     * Basic unpaginated POST search (raw Amadeus response)
     */
    @PostMapping("/search")
    public ResponseEntity<List<FlightOfferResponse>> searchFlights(@RequestBody FlightSearchRequestDto request) {
        try {
            List<FlightOfferResponse> results = flightSearchService.searchFlights(request);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> getFlightById(@PathVariable String id) {
        FlightDTO flight = flightSearchService.getFlightById(id);
        if (flight == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(flight);
    }

    @PostMapping("/search/paginated")
    public ResponseEntity<PaginatedResponse<FlightDTO>> searchFlightsPaginated(
            @Valid @RequestBody FlightSearchRequestDto request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            PaginatedResponse<FlightDTO> result = flightSearchService.searchFlightsWithPagination(request, page, size);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new PaginatedResponse<>());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new PaginatedResponse<>());
        }
    }


    /**
     * GET version of paginated flight search using query parameters
     */
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<FlightDTO>> searchFlightsGet(
            @RequestParam String originLocationCode,
            @RequestParam String destinationLocationCode,
            @RequestParam String departureDate,
            @RequestParam(required = false) String returnDate,
            @RequestParam(defaultValue = "1") int adults,
            @RequestParam(defaultValue = "USD") String currency,
            @RequestParam(defaultValue = "false") boolean nonStop,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            FlightSearchRequestDto request = new FlightSearchRequestDto(
                    originLocationCode,
                    destinationLocationCode,
                    departureDate,
                    returnDate,
                    adults,
                    currency,
                    nonStop,
                    sortBy,
                    sortOrder
            );

            PaginatedResponse<FlightDTO> result = flightSearchService.searchFlightsWithPagination(request, page, size);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new PaginatedResponse<>());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new PaginatedResponse<>());
        }
    }
}