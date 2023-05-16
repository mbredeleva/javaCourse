package dataStructures;

import lombok.Data;
@Data
public class EmptyResponse {
    int statusCode;
    public EmptyResponse(int statusCode){
        this.statusCode = statusCode;
    }
}
