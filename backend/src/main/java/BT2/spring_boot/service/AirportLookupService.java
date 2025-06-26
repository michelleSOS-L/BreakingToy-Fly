package BT2.spring_boot.service;

import BT2.spring_boot.modelDto.AirportDtop;
import java.util.List;

public interface AirportLookupService {
    List<AirportDtop> searchAirports(String keyword);
}
