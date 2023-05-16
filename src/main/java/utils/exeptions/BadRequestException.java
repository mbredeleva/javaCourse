package utils.exeptions;

import org.apache.http.HttpException;

public class BadRequestException extends HttpException {
    public BadRequestException(String message) {
        super(message);
    }
}
