package BT2.spring_boot.modelDto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FlightSearchRequestDto {

    @NotBlank(message = "Origin code is required")
    @Size(min = 3, max = 3, message = "Origin code must be 3 letters")
    private String originLocationCode;

    @NotBlank(message = "Destination code is required")
    @Size(min = 3, max = 3, message = "Destination code must be 3 letters")
    private String destinationLocationCode;

    @NotBlank(message = "Departure date is required")
    private String departureDate;

    private String returnDate;

    @Min(value = 1, message = "At least one adult is required")
    private int adults;

    @Pattern(regexp = "USD|MXN|EUR", message = "Currency must be USD, MXN, or EUR")
    private String currency;

    private boolean nonStop;

    private String sortBy;
    private String sortOrder;
}
