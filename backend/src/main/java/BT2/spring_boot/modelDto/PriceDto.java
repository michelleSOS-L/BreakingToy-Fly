package BT2.spring_boot.modelDto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {
    private String currency;
    private String total;
    private String base;
    private List<FeeDto> fees;
}
