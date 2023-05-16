package dataStructures;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PassengerRequest {
    String name;
    int trips;
    int airline;

    public PassengerRequest(String name, int airline, int trips) {
        this.name = name;
        this.trips = trips;
        this.airline = airline;
    }
}
