package dataStructures;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyResponseItemList {
    private List<Object> data;
    private int totalPages;
    private int totalPassengers;
}
