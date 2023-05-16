package restUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import utils.exeptions.*;
import io.restassured.response.Response;
import io.restassured.specification.*;

import static utils.StringConstants.MY_ENDPOINT;


public class RestMethods {

    public static Response get(RequestSpecification spec) throws HttpException {
        return parseHttpResponse(spec.get().then().log().all().
                extract().response());
    }

    public static Response post(RequestSpecification spec, Object myBody) throws HttpException {
        return parseHttpResponse(spec.body(myBody).post().then().log().all().
                extract().response());
    }

    public static Response put(RequestSpecification spec, Object myBody) throws HttpException {
        return parseHttpResponse(spec.body(myBody).put().then().log().all().
                extract().response());
    }

    public static Response delete(RequestSpecification spec) throws HttpException {
        return parseHttpResponse(
                spec.delete().then().log().all().
                        extract().response());
    }

    private static Response parseHttpResponse(Response resp) throws HttpException{
        int code = resp.getStatusCode();
        switch(code){
            case 200: case 201: case 202: case 203: case 204: case 205: case 206: case 207: case 208:
                return resp;
            case 400:
                throw new BadRequestException(resp.path("message"));
            case 401:
                throw new UnauthorizedException(resp.path("message"));
            // Here should be all other exceptions
            default:
                throw new HttpException("Something strange happened");
        }
    }
}
