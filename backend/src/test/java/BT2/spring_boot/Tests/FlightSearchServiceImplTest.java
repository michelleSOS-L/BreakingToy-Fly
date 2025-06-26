package BT2.spring_boot.Tests;

import BT2.spring_boot.modelDto.*;
import BT2.spring_boot.service.AmadeusAuthService;
import BT2.spring_boot.service.impl.FlightSearchServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FlightSearchServiceImplTest {

    private AmadeusAuthService authService;
    private WebClient.Builder webClientBuilder;
    private WebClient webClient;
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;
    private ObjectMapper objectMapper;
    private FlightSearchServiceImpl flightSearchService;

    @BeforeEach
    void setUp() {
        authService = mock(AmadeusAuthService.class);
        webClientBuilder = mock(WebClient.Builder.class);
        webClient = mock(WebClient.class);
        requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);
        objectMapper = new ObjectMapper();

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        flightSearchService = new FlightSearchServiceImpl(authService, webClientBuilder, objectMapper);
    }

    @Test
    void searchFlights_shouldReturnEmptyListOnException() throws Exception {
        when(authService.getAccessToken()).thenReturn("mock-token");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri((URI) any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(eq(String.class))).thenReturn(Mono.just("invalid-json"));

        FlightSearchRequestDto request = new FlightSearchRequestDto(
                "MEX", "JFK", "2025-07-01", null, 1, "USD", false, null, null
        );

        List<FlightOfferResponse> result = flightSearchService.searchFlights(request);
        assertEquals(Collections.emptyList(), result);
    }


}