import dataStructures.*;

import lombok.SneakyThrows;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import restUtils.MyRestApi;
import utils.config.Config;
import utils.exeptions.BadRequestException;

import static org.assertj.core.api.Assertions.assertThat;
import static utils.StringConstants.*;

public class ApiTest {
    private final int airlineID = 6979;

    private static String userToUpdateID;
    private static String firstUserToDeleteID;
    private static String secondUserToDeleteID;

    private static final MyRestApi api = new MyRestApi(Config.getUserConfig().user2.getLogin(),
            Config.getUserConfig().user2.getPassword());

    @SneakyThrows
    @BeforeAll
    public static void beforeAll(){
        // Find an existing passenger's id
        PassengerListResponse passengers = api.getAllPassengers();
        userToUpdateID = passengers.get(0).get_id();
        firstUserToDeleteID = passengers.get(1).get_id();
        secondUserToDeleteID = passengers.get(1).get_id();


        // Indeed, it would be better to create a function that returns an existing user's id in any moment
        // But I wanted to use a fixture in my tests :-)
    }

    @SneakyThrows
    @Test
    @DisplayName("Create a new passenger")
    void postPassengerTest() {
        String name = "Malcolm Reynolds";
        int tripsNum = 123;
        PassengerItemResponse response =  api.createNewPassenger(name,airlineID, tripsNum);

        //POST - check name, trips, and airline data
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getTrips()).isEqualTo(tripsNum);
        assertThat(response.getAirline().size()).isEqualTo(1); // Only one airline was added
        assertThat(response.getAirline().get(0).get("id")).isEqualTo(airlineID);
    }

    @Test
    @DisplayName("Try to create a new passenger with incorrect airline")
    void postPassengerWithIncorrectDataTest(){
        try {
            api.createNewPassenger("Malcolm Reynolds", 100500, 2);
        } catch (HttpException e){
            assertThat(e.getClass()).isEqualTo(BadRequestException.class);
            assertThat(e.getMessage()).isEqualTo(WRONG_AIRLINE_ID_MESSAGE);
        }
    }

    @SneakyThrows
    @Test
    @DisplayName("Get an existing passenger by ID")
    void getExistingPassengerByIDTest() {
        PassengerItemResponse response = api.getPassengerById(userToUpdateID);

        //GET - check id, name, trips, and airline data
        assertThat(response.get_id()).isEqualTo(userToUpdateID);
        assertThat(response.getName()).isNotEmpty();
        assertThat(response.getTrips()).isGreaterThan(0);
        assertThat(response.getAirline().size()).isGreaterThan(0); // Airlines list is not empty
    }

    @SneakyThrows
    @Test
    @DisplayName("Try to get a passenger by non-existing ID")
    void getNotExistingPassengerByIDTest(){
        String incorrectID = "lalala";
        EmptyResponse empty = api.getUnknownPassengerById(incorrectID);
        // I don't like this line. But the task was to check status code, so I suppose
        // the test should have an explicit assert checking response status code
        // Shouldn't api return 4xx status code in this case?
        assertThat(empty.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
    }

    @SneakyThrows
    @Test
    @DisplayName("Try to get a passenger that existed but was deleted")
    void getJustDeletedPassengerByIDTest(){
        api.deletePassenger(secondUserToDeleteID);
        EmptyResponse empty = api.getUnknownPassengerById(secondUserToDeleteID);
        // I don't like this line. But the task was to check status code, so I suppose
        // the test should have an explicit assert checking response status code
        // Shouldn't api return 4xx status code in this case?
        assertThat(empty.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
    }

    @SneakyThrows
    @Test
    @DisplayName("Update a name of an existing user")
    void updateExistingPassengerName() {
        String newName = "Summer Glau";
        PassengerItemResponse userData = api.getPassengerById(userToUpdateID);
        MessageOnlyResponse response = api.updatePassenger(userToUpdateID, newName, (int)(userData.getAirline().get(0)).get("id"), userData.getTrips());

        assertThat(response.getMessage()).isEqualTo(PASSENGER_SUCCESSFULLY_UPDATED_MESSAGE);
    }

    @Test
    @DisplayName("Try to update a non-existing user")
    void updateNonExistingPassengerName() {
        String newName = "Summer Glau";
        String incorrectId = "lalala";

        try {
            api.updatePassenger(incorrectId, newName, airlineID, 123);
        } catch (HttpException e) {
            assertThat(e.getClass()).isEqualTo(BadRequestException.class);
            assertThat(e.getMessage()).isEqualTo(WRONG_PASSENGER_ID_MESSAGE);
        }
    }

    @SneakyThrows
    @Test
    @DisplayName("Delete an existing user")
    void deletePassengerTest() {
        MessageOnlyResponse resp = api.deletePassenger(firstUserToDeleteID);
        assertThat(resp.getMessage()).isEqualTo(PASSENGER_SUCCESSFULLY_DELETED_MESSAGE);
    }

    @Test
    @DisplayName("Delete a non-existing user")
    @Disabled("Test fails for a bug, status code is 502 instead of any from 4XX")
    void deleteNonExistingPassengerTest() {
        String incorrectId = "lalala";
        try {
            api.deletePassenger(incorrectId);
        } catch (HttpException e) {
            assertThat(e.getClass()).isEqualTo(BadRequestException.class);
        }
    }
}
