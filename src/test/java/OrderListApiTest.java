import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;

public class OrderListApiTest {

    private Order order;
    private InfoForOrder infoForOrder;
    private OrderClient orderClient;

    @Before
    public void setUp() {


        orderClient = new OrderClient();
        RestAssured.baseURI = "https://qa-mesto.praktikum-services.ru";

    }

    @Test
    public void getOrderListApiTest(){
        String[] colorScooter = new String[] {"BLACK"};
        infoForOrder = OrderGenerate.getDefaultOrderInfo(colorScooter);
        ValidatableResponse response = orderClient.create(infoForOrder); // создаем первый заказ
        response = orderClient.create(infoForOrder); // создаем второй заказ

        ValidatableResponse responseOrderList = orderClient.getOrderList(); // получаем список заказов

        OrderList ordersList = responseOrderList.extract().body().as(OrderList.class);
        List<Order> orders = ordersList.getCreatedOrders();
        int quantityOrders = orders.size();

        assertEquals(true, quantityOrders >= 2);
    }
}
