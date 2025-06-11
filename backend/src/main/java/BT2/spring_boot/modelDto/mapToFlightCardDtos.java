package BT2.spring_boot.modelDto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class mapToFlightCardDtos {

    private List<FlightCardDto> mapToFlightCardDtos(String rawJson) {
        List<FlightCardDto> cards = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(rawJson);
            JsonNode data = root.get("data");

            if (data != null && data.isArray()) {
                for (JsonNode offer : data) {
                    FlightCardDto dto = new FlightCardDto();
                    dto.setId(offer.get("id").asText());

                    JsonNode itineraries = offer.get("itineraries");
                    JsonNode segments = itineraries.get(0).get("segments");

                    JsonNode firstSegment = segments.get(0);
                    JsonNode lastSegment = segments.get(segments.size() - 1);

                    dto.setDepartureAirport(firstSegment.get("departure").get("iataCode").asText());
                    dto.setArrivalAirport(lastSegment.get("arrival").get("iataCode").asText());
                    dto.setDepartureTime(firstSegment.get("departure").get("at").asText());
                    dto.setArrivalTime(lastSegment.get("arrival").get("at").asText());

                    String carrierCode = firstSegment.get("carrierCode").asText();
                    dto.setAirline(carrierCode); // You can enhance this later using the Airline Info API

                    dto.setTotalDuration(itineraries.get(0).get("duration").asText());

                    JsonNode priceNode = offer.get("price");
                    dto.setPrice(priceNode.get("total").asText() + " " + priceNode.get("currency").asText());

                    cards.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }
}
