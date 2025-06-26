package BT2.spring_boot.modelDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirportResponseWrapper {
    private List<AirportDtop> data;
}
