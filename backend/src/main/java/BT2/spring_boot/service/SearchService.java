package BT2.spring_boot.service;

import BT2.spring_boot.modelDto.AirportResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SearchService {

    private final AmadeusAuthService authService;
    private final WebClient.Builder webClientBuilder;

    @Value("${amadeus.api.location}")
    private String apiUrl;

    public SearchService(AmadeusAuthService authService, WebClient.Builder webClientBuilder) {
        this.authService = authService;
        this.webClientBuilder = webClientBuilder;
    }

    public String getAirports(String keyword) throws JSONException {
        String token = authService.getAccessToken();
        WebClient client = webClientBuilder.build();

        return client.get()
                .uri(apiUrl + "/v1/reference-data/locations?subType=AIRPORT&keyword=" + keyword)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
