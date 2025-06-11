package BT2.spring_boot.modelDto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightOfferDto {
    private String id;
    private String source;
    private boolean oneWay;
    private int numberOfBookableSeats;
    private PriceDto price;
    private List<ItineraryDto> itineraries;
    private List<TravelerPricingDto> travelerPricing;

}
