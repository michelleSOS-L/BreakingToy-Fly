package BT2.spring_boot.service.impl;

import BT2.spring_boot.modelDto.AirlineDTO;
import BT2.spring_boot.service.AirlineService;
import BT2.spring_boot.service.AmadeusAuthService;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AirlineServiceImpl implements AirlineService {

    private final WebClient.Builder webClientBuilder;
    private final AmadeusAuthService authService;

    public AirlineServiceImpl(WebClient.Builder webClientBuilder, AmadeusAuthService authService) {
        this.webClientBuilder = webClientBuilder;
        this.authService = authService;
    }

    @Override
    public AirlineDTO getAirlineInfo(String code) throws JSONException {
        String token = authService.getAccessToken();

        String response = webClientBuilder.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("test.api.amadeus.com")
                        .path("/v1/reference-data/airlines")
                        .queryParam("airlineCodes", code)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONObject json = new JSONObject(response);
        JSONObject data = json.getJSONArray("data").getJSONObject(0);
        return new AirlineDTO(data.getString("iataCode"), data.getString("businessName"));
    }
}
