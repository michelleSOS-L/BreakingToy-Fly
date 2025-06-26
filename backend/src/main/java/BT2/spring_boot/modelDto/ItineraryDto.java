package BT2.spring_boot.modelDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Data
@Getter
@Setter
public class ItineraryDto {
    private String duration;
    private List<SegmentDto> segments;

}