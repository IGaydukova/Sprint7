import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class OrderGenerate {

    public static InfoForOrder getDefaultOrderInfo(String[] colorScooter){
        Faker faker = new Faker();
        return new InfoForOrder(
                faker.name().firstName(),  //firstName
                faker.name().lastName(), //lastName
                faker.address().fullAddress(), //address
                faker.address().streetName(), //metroStation
                faker.phoneNumber().phoneNumber(), //phone
                new Random().nextInt(300),  //rentTime
                "2022-0"+RandomStringUtils.randomNumeric(1)+"-0"+RandomStringUtils.randomNumeric(1),//deliveryDate
                colorScooter,
                RandomStringUtils.randomAlphanumeric(30)//comment
                );
    }

}
