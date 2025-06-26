package BT2.spring_boot.service;

import BT2.spring_boot.modelDto.AirlineDTO;
import java.util.Optional;

public interface AirlineLookupService {
    Optional<AirlineDTO> findAirlineByCode(String code);
}
