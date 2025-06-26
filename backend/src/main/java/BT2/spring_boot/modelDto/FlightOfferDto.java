package BT2.spring_boot.modelDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightOfferDto {
    private String airlineName;
    private String airlineCode;
    private String departureTime;
    private String arrivalTime;
    private String originAirport;
    private String destinationAirport;
    private String duration;
    private String totalPrice;
    private double pricePerAdult;
}