package ClientServer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class Client {
    //Constructor
    private Client() {}

    //Methods
    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 15000);
            ServerInter stub = (ServerInter) registry.lookup("ServerInter");
            String response = stub.sayHello();
            System.out.println("Server says: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}