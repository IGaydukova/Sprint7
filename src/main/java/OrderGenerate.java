import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class OrderGenerate {

    public static InfoForOrder getDefaultOrderInfo(String[] colorScooter){

        return new InfoForOrder(
                RandomStringUtils.randomAlphanumeric(10),//firstName
                RandomStringUtils.randomAlphanumeric(10),//lastName
                RandomStringUtils.randomAlphanumeric(10),//address
                RandomStringUtils.randomNumeric(1),//metroStation
                RandomStringUtils.randomNumeric(10),//phone
                new Random().nextInt(300),  //rentTime
                "2022-0"+RandomStringUtils.randomNumeric(1)+"-0"+RandomStringUtils.randomNumeric(1),//deliveryDate
                colorScooter,
                RandomStringUtils.randomAlphanumeric(30)//comment
                );
    }

}
