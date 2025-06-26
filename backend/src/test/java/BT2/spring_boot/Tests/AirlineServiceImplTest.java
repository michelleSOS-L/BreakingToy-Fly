package BT2.spring_boot.Tests;

import BT2.spring_boot.modelDto.AirlineDTO;
import BT2.spring_boot.service.AmadeusAuthService;
import BT2.spring_boot.service.impl.AirlineServiceImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AirlineServiceImplTest {

    private AirlineServiceImpl airlineService;
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
        airlineService = new AirlineServiceImpl(webClientBuilder, authService);
    }

    @Test
    void getAirlineInfo_shouldReturnDTO_whenResponseIsValid() throws JSONException, org.springframework.boot.configurationprocessor.json.JSONException {
        String json = "{\"data\":[{\"iataCode\":\"AA\",\"businessName\":\"American Airlines\"}]}";

        when(authService.getAccessToken()).thenReturn("token");
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri((URI) any())).thenReturn(headersSpec);
        when(headersSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(eq(String.class))).thenReturn(Mono.just(json));

        AirlineDTO result = airlineService.getAirlineInfo("AA");

        assertEquals("AA", result.getCode());
        assertEquals("American Airlines", result.getName());
    }

    @Test
    void getAirlineInfo_shouldThrowException_onInvalidJson() throws org.springframework.boot.configurationprocessor.json.JSONException {
        when(authService.getAccessToken()).thenReturn("token");
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri((URI) any())).thenReturn(headersSpec);
        when(headersSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(eq(String.class))).thenReturn(Mono.just("invalid"));

        assertThrows(JSONException.class, () -> airlineService.getAirlineInfo("AA"));
    }
}
