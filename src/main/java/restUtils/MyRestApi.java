package restUtils;

import dataStructures.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import utils.config.Config;

import java.util.ArrayList;
import java.util.Objects;

import static io.restassured.RestAssured.given;

public class MyRestApi {
    private final String baseUrl = Config.getServiceConfig().apiUrl() + "/passenger";
    private final String loginUrl = Config.getServiceConfig().loginUrl();
    private Headers headers;
    private final String clientId = Config.getServiceConfig().clientId();

    public MyRestApi(String login, String password) {
        new MyRestApi();
        Header authHeader = new Header("Authorization", "Bearer " + getToken(login, password));
        ArrayList<Header> newHeaders = new ArrayList<>();
        if (!Objects.isNull(headers)){
            newHeaders = (ArrayList<Header>) headers.asList();
        }
        newHeaders.add(authHeader);
        headers = new Headers(newHeaders);
    }

    public MyRestApi(){
    }

    public String getToken(String login, String password) {

        Response resp = given().
                contentType(ContentType.URLENC).
                formParam("grant_type", "password").
                formParam("scope", "offline_access").
                formParam("client_id", clientId).
                formParam("username", login).
                formParam("password", password).
                when().post(loginUrl).
                then().log().all().
                extract().response();;

        return resp.then().statusCode(HttpStatus.SC_OK).extract().path("access_token");
    }

    public PassengerListResponse getAllPassengers() throws HttpException {
        System.out.println();
        Response resp = RestMethods.get(getDefaultRequestSpecification());
        return resp.as(PassengerListResponse.class);
    }

    public PassengerItemResponse getPassengerById(String id) throws HttpException {
        Response resp = RestMethods.get(getDefaultRequestSpecification().basePath("/" + id));
        return resp.as(PassengerItemResponse.class);
    }

    public EmptyResponse getUnknownPassengerById(String id) throws HttpException {
        Response resp = RestMethods.get(getDefaultRequestSpecification().basePath("/" + id));
        return new EmptyResponse(resp.getStatusCode());
    }

    public PassengerItemResponse createNewPassenger(String name, int airlineID, int tripsNum) throws HttpException {
        PassengerRequest myBody = new PassengerRequest(name,airlineID, tripsNum);
        Response resp = RestMethods.post(getDefaultRequestSpecification(), myBody);
        return resp.as(PassengerItemResponse.class);
    }

    public MessageOnlyResponse updatePassenger(String id, String name, int airlineID, int tripsNum) throws HttpException {
        PassengerRequest myBody = new PassengerRequest(name,airlineID, tripsNum);
        Response resp = RestMethods.put(getDefaultRequestSpecification().basePath("/" + id), myBody);
        return resp.as(MessageOnlyResponse.class);
    }

    public MessageOnlyResponse deletePassenger(String id) throws HttpException {
        Response resp = RestMethods.delete(getDefaultRequestSpecification().basePath("/" + id));
        return resp.as(MessageOnlyResponse.class);
    }
    private RequestSpecification getDefaultRequestSpecification(){
        return RestAssured.given().baseUri(baseUrl).basePath("/").
                headers(headers).contentType(ContentType.JSON);
    }
}
