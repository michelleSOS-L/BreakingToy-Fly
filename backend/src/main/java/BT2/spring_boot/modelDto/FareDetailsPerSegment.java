package BT2.spring_boot.modelDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FareDetailsPerSegment {
    private String segmentId;
    private String cabin;
    private String fareBasis;
    private String brandedFare;
    private String class_;
    private IncludedCheckedBagsDto includedCheckedBags;

    // Getters and setters
}