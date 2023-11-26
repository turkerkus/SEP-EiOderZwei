package ClientServer;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
public class Server implements Remote, ServerInter {
    //Variables
    public static Registry registry = null;

    //Constructor
    public Server() throws RemoteException {
        super();
    }

    //Methods
    public String sayHello() throws RemoteException {
        return "Connection to Server successful. Hello Client!";
    }

    public static void main(String[] args) {
        try {
            Server obj = new Server();
            ServerInter stub = (ServerInter) UnicastRemoteObject.exportObject(obj, 0);
            registry = LocateRegistry.createRegistry(15000);
            registry.bind("ServerInter", stub);

            System.err.println("Server ready");
        }
        catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
