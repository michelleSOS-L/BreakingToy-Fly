package BT2.spring_boot.controller;

import BT2.spring_boot.modelDto.FlightCardDto;
import BT2.spring_boot.service.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    private final SearchService airportService;

    public AirportController(SearchService airportService) {
        this.airportService = airportService;
    }

    @GetMapping
    public String getAirports(@RequestParam String keyword) {
        return airportService.searchAirports(keyword);
    }
    @GetMapping("/flights")
    public List<FlightCardDto> getFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String date,
            @RequestParam int adults,
            @RequestParam String currency,
            @RequestParam boolean nonStop
    ) {
        return airportService.searchFlightsSimplified(origin, destination, date, adults, currency, nonStop);
    }
}