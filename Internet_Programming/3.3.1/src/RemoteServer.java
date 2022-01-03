import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface RemoteServer extends Remote {
  void addBall() throws RemoteException;
  
  void pauseBalls() throws RemoteException;
  
  Vector<Object> getBalls() throws RemoteException;
}