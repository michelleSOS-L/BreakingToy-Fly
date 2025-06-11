package BT2.spring_boot.modelDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirportDto {
    private String iataCode;
    private String name;
    private String detailedName;
    private String country;
    private String city;
    private String subType;
}
