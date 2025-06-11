package BT2.spring_boot.modelDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelerPricingDto {
    private String travelerId;
    private String fareOption;
    private String travelerType;
    private String totalPrice;
    private String basePrice;
    private String currency;
}
