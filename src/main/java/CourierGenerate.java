import java.util.Random;
import com.github.javafaker.Faker;
public class CourierGenerate {

    public static Courier getDefaultCourier(){
        Faker faker = new Faker();
        return new Courier(faker.name().username(), faker.internet().password(), faker.name().firstName());}

    public static Courier getCourierWithoutPassword(){
        Faker faker = new Faker();
        return new Courier(faker.name().username(), null, faker.name().firstName());
    }

    public static Courier getCourierWithoutLogin(){
        Faker faker = new Faker();
        return new Courier(null, faker.internet().password(), faker.name().firstName());
    }

   /* public static Courier getRundomCourier(){
        return new Courier("Courier"+new Random().nextInt(1000), "pass"+new Random().nextInt(100), "RundomCourier");

     }*/

}
