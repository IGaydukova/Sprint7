//import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

public class CourierClient extends RestClient {
   private static final String COURIER_URL = "/api/v1/courier";
   private static final String DELETE_URL = "/api/v1/courier/";


   private static final String LOGIN_URL = "/api/v1/courier/login";

   // @Step("Create new courier: {courier}")
    public ValidatableResponse create(Courier courier){
        return given()
                .spec(getBaseSpec())
                //.header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_URL)
                .then();
    }

    public ValidatableResponse login(CourierCredentials credentials){
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(LOGIN_URL)
                .then();
    }

    public ValidatableResponse delete (int id) {
        return given()
                .spec(getBaseSpec())
                .pathParam("id", id)
                .when()
                .delete(DELETE_URL+"{id}")
                .then();
    }
}
