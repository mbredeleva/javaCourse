package dataStructures;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Data
@Builder
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PassengerItemResponse {
    private String _id;
    private String name;
    private int trips;
    private ArrayList<LinkedHashMap<String, Object>> airline;
    private int __v;
}
