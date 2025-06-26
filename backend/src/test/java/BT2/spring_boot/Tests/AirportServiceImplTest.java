package BT2.spring_boot.Tests;

import BT2.spring_boot.modelDto.AirportDtop;
import BT2.spring_boot.service.AmadeusAuthService;
import BT2.spring_boot.service.impl.AirportServiceImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AirportServiceImplTest {

    private AirportServiceImpl airportService;
    private WebClient.Builder webClientBuilder;
    private WebClient webClient;
    private WebClient.RequestHeadersUriSpec uriSpec;
    private WebClient.RequestHeadersSpec headersSpec;
    private WebClient.ResponseSpec responseSpec;
    private AmadeusAuthService authService;

    @BeforeEach
    void setup() {
        webClientBuilder = mock(WebClient.Builder.class);
        webClient = mock(WebClient.class);
        uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        headersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);
        authService = mock(AmadeusAuthService.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        airportService = new AirportServiceImpl(webClientBuilder, authService);
    }

    @Test
    void searchAirports_shouldReturnList_whenValidResponse() throws JSONException, org.springframework.boot.configurationprocessor.json.JSONException {
        String json = """
            {
              "data": [
                {
                  "iataCode": "MEX",
                  "address": { "cityName": "Mexico City" },
                  "name": "Benito Juarez International Airport"
                }
              ]
            }
        """;

        when(authService.getAccessToken()).thenReturn("token");
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri((URI) any())).thenReturn(headersSpec);
        when(headersSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(eq(String.class))).thenReturn(Mono.just(json));

        List<AirportDtop> result = airportService.searchAirports("mex");
        assertEquals(1, result.size());
        assertEquals("MEX", result.get(0).getIataCode());
    }

    @Test
    void searchAirports_shouldThrowException_onError() throws org.springframework.boot.configurationprocessor.json.JSONException {
        when(authService.getAccessToken()).thenReturn("token");
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri((URI) any())).thenReturn(headersSpec);
        when(headersSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(eq(String.class))).thenReturn(Mono.just("bad"));

        assertThrows(RuntimeException.class, () -> airportService.searchAirports("xxx"));
    }
}


