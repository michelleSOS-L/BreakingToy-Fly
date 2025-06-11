package BT2.spring_boot.modelDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightSegmentDto {
    private String departureAirport;
    private String departureTime;
    private String arrivalAirport;
    private String arrivalTime;
    private String carrierCode;
    private String carrierName;
    private String flightNumber;
    private String operatingCarrierCode;
    private String aircraftCode;
    private String duration;
}
