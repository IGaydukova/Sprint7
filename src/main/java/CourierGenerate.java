import java.util.Random;
public class CourierGenerate {

    public static Courier getDefaultCourier(){
        return new Courier("testlogin", "testpass", "testFirstName");
    }
    public static Courier getCourierWithoutPassword(){
        return new Courier("testlogin", null, "testFirstName");
    }

    public static Courier getCourierWithoutLogin(){
        return new Courier(null, "testpass", "testFirstName");
    }

    public static Courier getRundomCourier(){
        return new Courier("Courier"+new Random().nextInt(1000), "pass"+new Random().nextInt(100), "RundomCourier");

      }

}
