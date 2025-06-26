package BT2.spring_boot.modelDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirlineResponseWrapper {
    private List<AirlineDTO> data;
}
