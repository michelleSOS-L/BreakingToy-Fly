package BT2.spring_boot.modelDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TravelerPricingDto {
    private String travelerId;
    private String fareOption;
    private PriceDto price;
    private String travelerType;
    private FareDetailsPerSegment[] fareDetailsBySegment;
}
