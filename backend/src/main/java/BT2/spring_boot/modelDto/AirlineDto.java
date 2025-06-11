package BT2.spring_boot.modelDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirlineDto {
    private String iataCode;
    private String icaoName;
    private String businessName;
    private String commonName;
}
