package BT2.spring_boot.modelDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SegmentDto {
    private String arrivalAirport;
    private String carrierCode;
    private String flightNumber;
    private AircraftDto aircraft;
    private OperatingDto operating;
    private String duration;
    private String id;
    private int numberOfStops;
    private boolean blacklistedInEU;
    private String cabin;
    private String fareBasis;
    private String brandedFare;
    private String travelClass;
    private int includedCheckedBags;
    private List<AmenityDTO> amenities;
private LocationInfo departure;
private LocationInfo arrival;




}
