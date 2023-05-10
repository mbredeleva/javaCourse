import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class ApiTest {
    private static final String myEndpoint = "https://api.instantwebtools.net/v1/passenger";
    private final int airlineID = 2;

    private static String userToUpdate;
    private static String userToDelete;

    @BeforeAll
    public static void beforeAll(){
        // Find an existing passenger's id
        Response response = given().
                when().
                get(myEndpoint).
                then().log().all().
                statusCode(HttpStatus.SC_OK).
                body("totalPassengers", greaterThan(0)).
                extract().response();
        ArrayList data  = response.then().extract().path("data");
        Map<String, Object> passenger = (LinkedHashMap<String, Object>)data.get(0);
        userToUpdate = (String)passenger.get("_id");
        passenger = (LinkedHashMap<String, Object>)data.get(1);
        userToDelete = (String)passenger.get("_id");

        // Indeed, it would be better to create a function that returns an existing user's id in any moment
        // But I wanted to use a fixture in my tests :-)
    }

    @Test
    @DisplayName("Create a new passenger")
    void postPassengerTest() {
        Map<String, Object> myBody = new LinkedHashMap<>();
        myBody.put("name", "Malcolm Reynolds");
        myBody.put("trips", 2);
        myBody.put("airline", airlineID);

        Response response = given().
                contentType(ContentType.JSON).
                body(myBody).
                when().post(myEndpoint).
                then().log().all().
                statusCode(HttpStatus.SC_OK).
                extract().response();

        String id = response.then().extract().path("_id");
        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("Try to create a new passenger with incorrect airline")
    void postPassengerWithIncorrectDataTest() {
        Map<String, Object> myBody = new LinkedHashMap<>();
        myBody.put("name", "Malcolm Reynolds");
        myBody.put("trips", 2);
        myBody.put("airline", 100500); //TODO: Remove magic number

        Response response = given().
                contentType(ContentType.JSON).
                body(myBody).
                when().post(myEndpoint).
                then().log().all().
                statusCode(HttpStatus.SC_BAD_REQUEST).
                extract().response();
    }

    @Test
    @DisplayName("Get an existing passenger by ID")
    void getExistingPassengerByIDTest() {
        Response response = given().
                when().get(myEndpoint + "/" + userToUpdate).
                then().log().all().
                statusCode(HttpStatus.SC_OK).
                extract().response();

        String id = response.then().extract().path("_id");
        assertThat(id).isEqualTo(userToUpdate);
    }

    @Test
    @DisplayName("Try to get a passenger by non-existing ID")
    void getNotExistingPassengerByIDTest(){
        String incorrectID = "lalala";
        Response response = given().
                when().get(myEndpoint + "/" + incorrectID).
                then().log().all().
                statusCode(HttpStatus.SC_NO_CONTENT).
                extract().response();
    }

    @Test
    @DisplayName("Update a name of an existing user")
    void updateExistingPassengerName() {
        String newName = "Summer Glau";
        Response userData = given().
                when().get(myEndpoint + "/" + userToUpdate).
                then().log().all().
                statusCode(HttpStatus.SC_OK).
                extract().response();
        ArrayList aircompany = userData.then().extract().path("airline");
        Map<String, Object> myBody = new LinkedHashMap<>();
        myBody.put("name", newName);
        myBody.put("trips", userData.then().extract().path("trips"));
        myBody.put("airline", ((Map<String, Object>)aircompany.get(0)).get("id"));

        Response response = given().
                contentType(ContentType.JSON).
                body(myBody).
                when().put(myEndpoint + "/" + userToUpdate).
                then().log().all().
                statusCode(HttpStatus.SC_OK).
                extract().response();
    }

    @Test
    @DisplayName("Try to update a non-existing user")
    @Disabled("Probably a bug. Error code is 400, but should be 404")
    void updateNonExistingPassengerName() {
        String newName = "Summer Glau";
        String incorrectId = "lalala";
        Map<String, Object> myBody = new LinkedHashMap<>();
        myBody.put("name", newName);
        myBody.put("trips", 123);
        myBody.put("airline", airlineID);

        Response response = given().
                contentType(ContentType.JSON).
                body(myBody).
                when().put(myEndpoint + "/" + incorrectId).
                then().log().all().
                statusCode(HttpStatus.SC_NOT_FOUND).
                extract().response();
    }

    @Test
    @DisplayName("Delete an existing user")
    void deletePassengerTest() {
        given().
            when().delete(myEndpoint + "/" + userToDelete).
            then().log().all().
            statusCode(HttpStatus.SC_OK).
            extract().response();
    }

    @Test
    @DisplayName("Delete a non-existing user")
    @Disabled("Test fails for a bug, status code is 502 instead of any from 4XX")
    void deleteNonExistingPassengerTest() {
        String incorrectId = "lalala";
        given().
                when().delete(myEndpoint + "/" + incorrectId).
                then().log().all().
                statusCode(HttpStatus.SC_NOT_FOUND).
                extract().response();
    }
}
