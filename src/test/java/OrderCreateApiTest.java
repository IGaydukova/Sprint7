import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class OrderCreateApiTest {
    private InfoForOrder order;
    private OrderClient orderClient;
    private final String[] colorScooter;

    public OrderCreateApiTest(String[] colorScooter) {
        this.colorScooter = colorScooter;
    }


    @Parameterized.Parameters
    public static Object[] getOrderData() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GRAY"}},
                {new String[]{"BLACK", "GRAY"}},
                {new String[]{""}}
        };
    }

    @Before
    public void setUp() {
        order = OrderGenerate.getDefaultOrderInfo(colorScooter);
        orderClient = new OrderClient();
        RestAssured.baseURI = BasePage.URL;

    }
   // Создание заказа
    @Test
    public void orderCanCreateTest() {

        ValidatableResponse response = orderClient.create(order);
        int statusCode = response.extract().statusCode();
        assertEquals("The Response Code is incorrect", SC_CREATED, statusCode); // проверяем код ответа сервера
        assertNotNull("Order not contain Track",response.extract().path("track"));


    }
}
