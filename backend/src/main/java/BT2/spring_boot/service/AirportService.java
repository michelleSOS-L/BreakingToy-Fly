package BT2.spring_boot.service;

import BT2.spring_boot.modelDto.AirportDtop;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.util.List;

public interface AirportService {
    List<AirportDtop> searchAirports(String keyword) throws JSONException;
}
