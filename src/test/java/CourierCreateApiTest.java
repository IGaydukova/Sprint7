import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CourierCreateApiTest {

    private Courier courier;
    private CourierClient courierClient;
    private int courierID;

    @Before
    public void setUp() {
        courier = CourierGenerate.getDefaultCourier();
        courierClient = new CourierClient();
        RestAssured.baseURI = "https://qa-mesto.praktikum-services.ru";

    }
    @After
    public void tearDown(){
        courierClient.delete(courierID);
    }

    //курьера можно создать +
    @Test
        public void courierCanCreatedTest(){
        ValidatableResponse response = courierClient.create(courier);
        int statusCode = response.extract().statusCode();
        assertEquals("The Response Code is incorrect", SC_CREATED, statusCode); // проверяем код ответа сервера

        boolean isCreated = response.extract().path("ok");
        assertTrue("Courier not created", isCreated); // проверяем ответ Api
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Courier is not login", SC_OK, loginStatusCode);
        courierID = loginResponse.extract().path("id");
        assertNotNull("ID is not recieved", courierID); // логин получен

    }

    //Нельзя создать двух одинаковых курьеров +
    @Test
    public void sameCourierCanNotCreatedTest(){
        ValidatableResponse response = courierClient.create(courier); // создали курьера
        int statusCode = response.extract().statusCode();
        assertEquals("The Response Code is incorrect", SC_CREATED, statusCode); // проверяем код ответа сервера
        boolean isCreated = response.extract().path("ok");
        assertTrue("Courier not created", isCreated); // проверяем ответ Api

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = response.extract().statusCode();
       // assertEquals("Courier is not login", SC_OK, loginStatusCode);
        courierID = loginResponse.extract().path("id");
        assertNotNull("ID is not recieved", courierID); // логин получен (для дальнейшего удаления)

        ValidatableResponse responseSame = courierClient.create(courier); // создали еще одного такого же курьера
        int statusSameCode = responseSame.extract().statusCode();
        assertEquals("The Response Same Code is incorrect", SC_CONFLICT, statusSameCode); // проверяем код ответа сервера
        String textAnswer = responseSame.extract().path("message");
        assertEquals("The text of answer is incorrect", textAnswer, "Этот логин уже используется. Попробуйте другой.");
    }
    //Если создать пользователя с логином, который уже есть, возвращается ошибка +
    @Test
    public void courierCanNotCreateWithSameLoginTest(){
        ValidatableResponse response = courierClient.create(courier); // создали курьера
        int statusCode = response.extract().statusCode();
        assertEquals("The Response Code is incorrect", SC_CREATED, statusCode); // проверяем код ответа сервера
        boolean isCreated = response.extract().path("ok");
        assertTrue("Courier not created", isCreated); // проверяем ответ Api

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = response.extract().statusCode();
        // assertEquals("Courier is not login", SC_OK, loginStatusCode);
        courierID = loginResponse.extract().path("id");
        assertNotNull("ID is not recieved", courierID); // логин получен (для дальнейшего удаления)

        ValidatableResponse responseSame = courierClient.create(courier); // создали еще одного такого же курьера
        int statusSameCode = responseSame.extract().statusCode();
        assertEquals("The Response Same Code is incorrect", SC_CONFLICT, statusSameCode); // проверяем код ответа сервера

    }

    //чтобы создать курьера, нужно передать в ручку все обязательные поля;
    @Test
    public void courierCreateWithoutRequiredDate(){
        //убедились, что нельзя создать курьера без одного обязательного поля
        courier = CourierGenerate.getCourierWithoutLogin();
        ValidatableResponse responseWithoutLogin = courierClient.create(courier);
        int statusCode = responseWithoutLogin.extract().statusCode();
        assertEquals("The Response Code is incorrect", SC_BAD_REQUEST, statusCode); // проверяем код ответа сервера
        //убедились, что нельзя создать курьера без второго обязательного поля
        courier = CourierGenerate.getCourierWithoutPassword();
        ValidatableResponse responseWithoutPass = courierClient.create(courier);
        statusCode = responseWithoutPass.extract().statusCode();
        assertEquals("The Response Code is incorrect", SC_BAD_REQUEST, statusCode); // проверяем код ответа сервера
        //создаем курьера с 2-мя обязательными полями, без имени
        courier = new Courier("testlogin", "testpass", null);
        ValidatableResponse response = courierClient.create(courier);
        statusCode = response.extract().statusCode();
        assertEquals("The Response Code is incorrect", SC_CREATED, statusCode); // проверяем код ответа сервера

        boolean isCreated = response.extract().path("ok");
        assertTrue("Courier not created", isCreated); // проверяем ответ Api
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Courier is not login", SC_OK, loginStatusCode);
        courierID = loginResponse.extract().path("id");
        assertNotNull("ID is not recieved", courierID); // логин получен
    }


    //запрос возвращает правильный код ответа +
    @Test
    public void courierCreateAnswerCorrectTest(){
        ValidatableResponse response = courierClient.create(courier);
        int statusCode = response.extract().statusCode();
        assertEquals("The Response Code is incorrect", SC_CREATED, statusCode); // проверяем код ответа сервера

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierID = loginResponse.extract().path("id");
        assertNotNull("ID is not recieved", courierID); // логин получен
    }

    //Успешный запрос возвращает ok: true +
    @Test
    public void courierCreateAnswerTextTest(){
        ValidatableResponse response = courierClient.create(courier);
        boolean isCreated = response.extract().path("ok");
        assertTrue("Courier not created", isCreated); // проверяем ответ Api

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierID = loginResponse.extract().path("id");
        assertNotNull("ID is not recieved", courierID); // логин получен

    }
    //если одного из полей нет, запрос возвращает ошибку +
    //без логина
    @Test
    public void courierCreateWithoutLoginTest()    {
        courier = CourierGenerate.getCourierWithoutLogin();
        ValidatableResponse response = courierClient.create(courier);
        int statusCode = response.extract().statusCode();
        assertEquals("The Response Code is incorrect", SC_BAD_REQUEST, statusCode); // проверяем код ответа сервера
        String textAnswer = response.extract().path("message");
        assertEquals("The text of answer is incorrect", textAnswer, "Недостаточно данных для создания учетной записи");

    }
//без пароля
    @Test
    public void courierCreateWithoutPassTest()    {
        courier = CourierGenerate.getCourierWithoutPassword();
        ValidatableResponse response = courierClient.create(courier);
        int statusCode = response.extract().statusCode();
        assertEquals("The Response Code is incorrect", SC_BAD_REQUEST, statusCode); // проверяем код ответа сервера
        String textAnswer = response.extract().path("message");
        assertEquals("The text of answer is incorrect", textAnswer, "Недостаточно данных для создания учетной записи");

    }
 /*   public CourierClient getCourierClient() {
        return courierClient;
    }*/
}
