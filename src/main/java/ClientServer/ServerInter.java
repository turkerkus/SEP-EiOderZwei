package ClientServer;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface ServerInter extends Remote {
    String sayHello() throws RemoteException;
}