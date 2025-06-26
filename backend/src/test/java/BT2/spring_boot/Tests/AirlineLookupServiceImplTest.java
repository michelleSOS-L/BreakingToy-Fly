package BT2.spring_boot.Tests;

import BT2.spring_boot.modelDto.AirlineDTO;
import BT2.spring_boot.service.AmadeusAuthService;
import BT2.spring_boot.service.impl.AirlineLookupServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AirlineLookupServiceImplTest {

    private AirlineLookupServiceImpl airlineLookupService;
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

        airlineLookupService = new AirlineLookupServiceImpl(authService, webClientBuilder, objectMapper);
    }

    @Test
    void findAirlineByCode_shouldReturnDTO_whenDataExists() throws Exception {
        String json = "{\"data\":[{\"code\":\"UA\",\"name\":\"United\"}]}";
        when(authService.getAccessToken()).thenReturn("token");
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri((URI) any())).thenReturn(headersSpec);
        when(headersSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(eq(String.class))).thenReturn(Mono.just(json));

        Optional<AirlineDTO> result = airlineLookupService.findAirlineByCode("UA");
        assertTrue(result.isPresent());
        assertEquals("UA", result.get().getCode());
    }

    @Test
    void findAirlineByCode_shouldReturnEmpty_whenException() throws JSONException {
        when(authService.getAccessToken()).thenThrow(new RuntimeException("fail"));
        Optional<AirlineDTO> result = airlineLookupService.findAirlineByCode("FAIL");
        assertFalse(result.isPresent());
    }
}



