package BT2.spring_boot.Tests;

import BT2.spring_boot.modelDto.AirportDtop;
import BT2.spring_boot.modelDto.AirportResponseWrapper;
import BT2.spring_boot.service.AmadeusAuthService;
import BT2.spring_boot.service.impl.AirportLookupServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AirportLookupServiceImplTest {

    private AirportLookupServiceImpl airportLookupService;
    private AmadeusAuthService authService;
    private ObjectMapper objectMapper;
    private WebClient.Builder webClientBuilder;
    private WebClient webClient;
    private WebClient.RequestHeadersUriSpec uriSpec;
    private WebClient.RequestHeadersSpec headersSpec;
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setup() {
        authService = mock(AmadeusAuthService.class);
        objectMapper = new ObjectMapper();
        webClientBuilder = mock(WebClient.Builder.class);
        webClient = mock(WebClient.class);
        uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        headersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);

        airportLookupService = new AirportLookupServiceImpl(authService, objectMapper);
    }

    @Test
    void searchAirports_shouldReturnList_whenValidJson() throws Exception {
        String json = "{\"data\":[{\"iataCode\":\"CDG\",\"cityname\":\"Paris\",\"fullName\":\"Charles de Gaulle\"}]}";
        when(authService.getAccessToken()).thenReturn("token");
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri((URI) any())).thenReturn(headersSpec);
        when(headersSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(eq(String.class))).thenReturn(Mono.just(json));

        List<AirportDtop> result = airportLookupService.searchAirports("cdg");
        assertEquals(1, result.size());
        assertEquals("CDG", result.get(0).getIataCode());
    }

    @Test
    void searchAirports_shouldReturnEmptyList_onException() throws JSONException {
        when(authService.getAccessToken()).thenThrow(new RuntimeException("auth error"));

        List<AirportDtop> result = airportLookupService.searchAirports("invalid");
        assertTrue(result.isEmpty());
    }
}
