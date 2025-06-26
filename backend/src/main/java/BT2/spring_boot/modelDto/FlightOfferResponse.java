package BT2.spring_boot.modelDto;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FlightOfferResponse {
    private String type;
    private String id;
    private String source;
    private boolean instantTicketingRequired;
    private boolean nonHomogeneous;
    private boolean oneWay;
    private String lastTicketingDate;
    private int numberOfBookableSeats;
    private List<ItineraryDto> itineraries;
    private PriceDto price;
    private List<TravelerPricingDto> travelerPricings;


}

