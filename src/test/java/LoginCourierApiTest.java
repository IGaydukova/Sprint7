import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoginCourierApiTest {
    private Courier courier;
    private CourierClient courierClient;
    private int courierID;

    @Before
    public void setUp() {
        courier = CourierGenerate.getDefaultCourier();
        courierClient = new CourierClient();
        RestAssured.baseURI = BasePage.URL;
    }

    @After
    public void tearDown(){
        courierClient.delete(courierID);
    }

    //курьер может авторизоваться +
    @Test
    public void courierCanLoginTest(){
            ValidatableResponse response = courierClient.create(courier);
 // не уверена в необходимости 2-х следующих строк: имеет ли смысл проверять успешность создания курьера в тесте авторизации?
            int statusCode = response.extract().statusCode();
            assertEquals("The Response Code is incorrect", SC_CREATED, statusCode); // проверяем код ответа сервера
            ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
            int loginStatusCode = loginResponse.extract().statusCode();
            assertEquals("Courier is not login", SC_OK, loginStatusCode);
            courierID = loginResponse.extract().path("id");
            assertNotNull("ID is not recieved", courierID); // логин получен
    }
    //для авторизации нужно передать все обязательные поля + (баг в системе)
    @Test
    public void courierLoginAlsoAllRequiredDataTest(){
        ValidatableResponse response = courierClient.create(courier);
        int statusCode = response.extract().statusCode();
        assertEquals("The Response Code is incorrect", SC_CREATED, statusCode); // проверяем код ответа сервера
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Courier is not login", SC_OK, loginStatusCode);
        courierID = loginResponse.extract().path("id");
        assertNotNull("ID is not recieved", courierID); // логин получен
        //пробуем авторизоваться без пароля
        String path = courier.getPassword();
        courier.setPassword(null);
        loginResponse = courierClient.login(CourierCredentials.from(courier));
        loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("The Response Code of login without pass is incorrect", SC_NOT_FOUND, loginStatusCode);

        //пробуем авторизоваться без логина, предварительно вернув пароль
        courier.setPassword(path);
        courier.setLogin(null);
        loginResponse = courierClient.login(CourierCredentials.from(courier));
        loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("The Response Code of login without login is incorrect", SC_NOT_FOUND, loginStatusCode);
    }

    //система вернёт ошибку, если неправильно указать логин или пароль +
    @Test
    public void courierCanNotLoginIncorrectDataTest(){
        ValidatableResponse response = courierClient.create(courier);
        // не уверена в необходимости 2-х следующих строк: имеет ли смысл проверять успешность создания курьера в тесте авторизации?
        int statusCode = response.extract().statusCode();
        assertEquals("The Response Code is incorrect", SC_CREATED, statusCode); // проверяем код ответа сервера
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierID = loginResponse.extract().path("id");
        assertNotNull("ID is not recieved", courierID); // логин получен
        courier.setPassword("incorrectpass");
        loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Courier login with incorrect password", SC_NOT_FOUND, loginStatusCode);
    }
    //если какого-то поля нет, запрос возвращает ошибку +
    @Test
    public void courierAuthWithoutRequiredDateReturnErrorTest(){
        courier = CourierGenerate.getCourierWithoutLogin();
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Courier Auth without login return Incorrect code", SC_BAD_REQUEST, loginStatusCode);
        courier = CourierGenerate.getCourierWithoutPassword();
        loginResponse = courierClient.login(CourierCredentials.from(courier));
        loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Courier Auth without password return Incorrect code", SC_BAD_REQUEST, loginStatusCode);
    }
    //если авторизоваться под несуществующим пользователем, запрос возвращает ошибку +
    @Test
    public void courierLoginNotUserReturnErrorTest(){
        courier = CourierGenerate.getDefaultCourier();
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Courier error login for not user", SC_NOT_FOUND, loginStatusCode);
      }
    //успешный запрос возвращает id +
    @Test
    public void courierLoginReturnIDTest(){
        ValidatableResponse response = courierClient.create(courier);

        int statusCode = response.extract().statusCode();
        assertEquals("The Response Code is incorrect", SC_CREATED, statusCode); // проверяем код ответа сервера
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Courier is not login", SC_OK, loginStatusCode);
        courierID = loginResponse.extract().path("id");
        assertNotNull("ID is not recieved", courierID); // логин получен
    }

}