package BT2.spring_boot.modelDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PriceDto {
    private String currency;
    private String total;
    private String base;
}
