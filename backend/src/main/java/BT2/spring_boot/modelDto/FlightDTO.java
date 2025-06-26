package BT2.spring_boot.modelDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
    private String id;
    private String departureTime;
    private String arrivalTime;
    private String departureAirport;
    private String arrivalAirport;
    private String airlineCode;
    private String airlineName;
    private String operatingAirlineCode;
    private String operatingAirlineName;
    private String duration;
    private List<SegmentDto> segments;
    private List<AmenityDTO> amenities;
    private String currency;
    private double totalPrice;
    private double basePrice;
    private double fees;
    private double pricePerAdult;
    private List<String>layoverDurations;
    private List<SegmentDto>returnSegments;
    public Double getTotalPriceValue() {
        return totalPrice;
    }

    public long getTotalDurationInMinutes() {
        try {
            String[] parts = duration.replace("PT", "").split("H|M");
            int hours = duration.contains("H") ? Integer.parseInt(parts[0]) : 0;
            int minutes = duration.contains("M") ? Integer.parseInt(parts[parts.length - 1]) : 0;
            return hours * 60L + minutes;
        } catch (Exception e) {
            return Long.MAX_VALUE;
        }
    }
}