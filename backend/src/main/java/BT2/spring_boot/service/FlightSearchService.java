package BT2.spring_boot.service;


import BT2.spring_boot.modelDto.*;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.util.List;

public interface FlightSearchService {
    List<FlightOfferResponse> searchFlights(FlightSearchRequestDto request) throws JSONException;
    void validateSearchRequest(FlightSearchRequest request);
    PaginatedResponse<FlightDTO> searchFlightsWithPagination(FlightSearchRequestDto request, int page, int size);
   List<FlightDTO> maptOFlightDTOs(List<FlightOfferResponse>flightOffers, int adultCount);
    FlightDTO getFlightById(String id);
}


