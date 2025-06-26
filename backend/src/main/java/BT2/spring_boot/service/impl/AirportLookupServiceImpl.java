package BT2.spring_boot.service.impl;
import BT2.spring_boot.modelDto.AirportDtop;
import BT2.spring_boot.modelDto.AirportResponseWrapper;
import BT2.spring_boot.service.AmadeusAuthService;
import BT2.spring_boot.service.AirportLookupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Service
public class AirportLookupServiceImpl implements AirportLookupService {

    private final AmadeusAuthService amadeusAuthService;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${amadeus.api.airport-search}")
    private String airportSearchUrl;

    public AirportLookupServiceImpl(AmadeusAuthService amadeusAuthService,
                                    ObjectMapper objectMapper) {
        this.amadeusAuthService = amadeusAuthService;
        this.webClientBuilder = WebClient.builder();
        this.objectMapper = objectMapper;
    }

    @Override
    public List<AirportDtop> searchAirports(String keyword) {
        try {
            String token = amadeusAuthService.getAccessToken();
            String json = webClientBuilder.build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(airportSearchUrl)
                            .queryParam("keyword", keyword)
                            .queryParam("subType", "AIRPORT")
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            AirportResponseWrapper wrapper = objectMapper.readValue(json, AirportResponseWrapper.class);
            return wrapper.getData();

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
