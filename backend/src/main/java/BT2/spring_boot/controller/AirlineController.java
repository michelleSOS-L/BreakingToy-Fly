package BT2.spring_boot.controller;

import BT2.spring_boot.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/airlines")
public class AirlineController {
    private final SearchService searchService;
    public AirlineController(SearchService searchService) {
        this.searchService = searchService;
    }
    @GetMapping
    public String getAirlines(@RequestParam String code) {
        return searchService.getAirlineInfo(code);
    }
}
