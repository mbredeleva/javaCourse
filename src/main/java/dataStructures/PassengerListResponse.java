package dataStructures;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PassengerListResponse {
    private ArrayList<PassengerItemResponse> data;
    private int totalPages;
    private int totalPassengers;

    public PassengerItemResponse get(int i){
        return getData().get(i);
    }
}
