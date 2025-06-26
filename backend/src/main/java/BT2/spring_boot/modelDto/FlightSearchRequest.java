package BT2.spring_boot.modelDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class FlightSearchRequest {
    private String originLocationCode;
    private String destinationLocationCode;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private int adults;
    private String currencyCode;


    private boolean nonStop;

    public FlightSearchRequest() {}

    public FlightSearchRequest(String originLocationCode, String destinationLocationCode, LocalDate departureDate,
                               LocalDate returnDate, int adults, String currencyCode,  boolean nonStop) {
        this.originLocationCode = originLocationCode;
        this.destinationLocationCode = destinationLocationCode;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.adults = adults;
        this.currencyCode = currencyCode;
        this.nonStop = nonStop;
    }

}

