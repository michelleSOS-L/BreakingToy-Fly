package BT2.spring_boot.controller;

import BT2.spring_boot.modelDto.AirportDtop;

import BT2.spring_boot.service.AirportService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<AirportDtop>> searchAirports(@RequestParam String keyword) {
        try {
            List<AirportDtop> result = airportService.searchAirports(keyword);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

