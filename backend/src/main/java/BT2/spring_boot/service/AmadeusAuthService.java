package BT2.spring_boot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Map;

@Service
public class AmadeusAuthService {

    @Value("${amadeus.apiKey}")
    private String clientId;

    @Value("${amadeus.apiSecret}")
    private String clientSecret;

    private final WebClient webClient;

    public AmadeusAuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://test.api.amadeus.com").build();
    }

    public String getAccessToken() throws JSONException {
        String response = webClient.post()
                .uri("/v1/security/oauth2/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters
                        .fromFormData("grant_type", "client_credentials")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return new JSONObject(response).getString("access_token");
    }
}