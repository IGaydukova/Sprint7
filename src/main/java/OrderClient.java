//import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
   private static final String CREATE_ORDER_URL = "/api/v1/orders";

   // @Step("Create new order: {order}")
    public ValidatableResponse create(InfoForOrder order){
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(CREATE_ORDER_URL)
                .then();
    }

    public ValidatableResponse getOrderList(){
        return given()
                .spec(getBaseSpec())
                .when()
                .get(CREATE_ORDER_URL)
                .then();
    }

}
