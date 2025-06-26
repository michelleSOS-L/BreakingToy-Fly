package BT2.spring_boot.modelDto;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AirportResponseDTO {
    private List<AirportData> data;

    @Data
    public static class AirportData {
        private String iataCode;
        private String name;
    }
}
