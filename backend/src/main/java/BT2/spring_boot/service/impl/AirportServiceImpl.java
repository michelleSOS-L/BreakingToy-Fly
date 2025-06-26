package BT2.spring_boot.service.impl;

import BT2.spring_boot.modelDto.AirportDtop;
import BT2.spring_boot.service.AirportService;
import BT2.spring_boot.service.AmadeusAuthService;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
@Service
public class AirportServiceImpl implements AirportService {

    private final WebClient.Builder webClientBuilder;
    private final AmadeusAuthService authService;

    public AirportServiceImpl(WebClient.Builder webClientBuilder, AmadeusAuthService authService) {
        this.webClientBuilder = webClientBuilder;
        this.authService = authService;
    }

    @Override
    public List<AirportDtop> searchAirports(String keyword) throws JSONException {
        try {
            String token = authService.getAccessToken();

            String response = webClientBuilder.build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("test.api.amadeus.com")
                            .path("/v1/reference-data/locations")
                            .queryParam("keyword", keyword)
                            .queryParam("subType", "AIRPORT")
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("✈️ Raw response from Amadeus: " + response);

            if (response == null) {
                throw new RuntimeException("Amadeus response was null");
            }

            JSONObject jsonResponse = new JSONObject(response);
            JSONArray data = jsonResponse.getJSONArray("data");

            List<AirportDtop> results = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject airport = data.getJSONObject(i);
                results.add(new AirportDtop(
                        airport.getString("iataCode"),
                        airport.getJSONObject("address").optString("cityName", "Unknown"),
                        airport.optString("name", "Unknown Airport")
                ));
            }

            return results;

        } catch (Exception e) {
            System.err.println("❌ Failed to fetch or parse airport data");
            e.printStackTrace();
            throw new RuntimeException("Error in searchAirports", e);
        }
    }
}
