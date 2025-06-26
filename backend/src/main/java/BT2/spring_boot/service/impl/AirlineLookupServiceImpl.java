package BT2.spring_boot.service.impl;

import BT2.spring_boot.modelDto.AirlineDTO;
import BT2.spring_boot.modelDto.AirlineResponseWrapper;
import BT2.spring_boot.service.AmadeusAuthService;
import BT2.spring_boot.service.AirlineLookupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
public class AirlineLookupServiceImpl implements AirlineLookupService {

    private final AmadeusAuthService amadeusAuthService;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${amadeus.api.airline-info}")
    private String airlineUrl;

    public AirlineLookupServiceImpl(AmadeusAuthService amadeusAuthService,
                                    WebClient.Builder webClientBuilder,
                                    ObjectMapper objectMapper) {
        this.amadeusAuthService = amadeusAuthService;
        this.webClientBuilder = webClientBuilder;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<AirlineDTO> findAirlineByCode(String code) {
        try {
            String token = amadeusAuthService.getAccessToken();
            String json = webClientBuilder.build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(airlineUrl)
                            .queryParam("airlineCodes", code)
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            AirlineResponseWrapper wrapper = objectMapper.readValue(json, AirlineResponseWrapper.class);
            return wrapper.getData().stream().findFirst();

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}


