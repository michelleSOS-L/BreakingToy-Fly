package BT2.spring_boot.service;

import BT2.spring_boot.modelDto.AirlineDTO;
import org.springframework.boot.configurationprocessor.json.JSONException;

public interface AirlineService {
    AirlineDTO getAirlineInfo(String code) throws JSONException;
}
