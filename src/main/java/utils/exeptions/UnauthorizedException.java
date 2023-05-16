package utils.exeptions;
import org.apache.http.HttpException;

public class UnauthorizedException extends HttpException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
