import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;
import java.util.Vector;

public final class Server_Stub extends RemoteStub implements RemoteServer, Remote {
  private static final long serialVersionUID = 2L;
  
  private static Method $method_addBall_0;
  
  private static Method $method_getBalls_1;
  
  private static Method $method_pauseBalls_2;
  
  static {
    try {
      $method_addBall_0 = RemoteServer.class.getMethod("addBall", new Class[0]);
      $method_getBalls_1 = RemoteServer.class.getMethod("getBalls", new Class[0]);
      $method_pauseBalls_2 = RemoteServer.class.getMethod("pauseBalls", new Class[0]);
    } catch (NoSuchMethodException noSuchMethodException) {
      throw new NoSuchMethodError("stub class initialization failed");
    } 
  }
  
  public Server_Stub(RemoteRef paramRemoteRef) {
    super(paramRemoteRef);
  }
  
  public void addBall() throws RemoteException {
    try {
      this.ref.invoke(this, $method_addBall_0, null, -1066573037967864541L);
    } catch (RuntimeException runtimeException) {
      throw runtimeException;
    } catch (RemoteException remoteException) {
      throw remoteException;
    } catch (Exception exception) {
      throw new UnexpectedException("undeclared checked exception", exception);
    } 
  }
  
  public Vector getBalls() throws RemoteException {
    try {
      Object object = this.ref.invoke(this, $method_getBalls_1, null, 1585176364196827426L);
      return (Vector)object;
    } catch (RuntimeException runtimeException) {
      throw runtimeException;
    } catch (RemoteException remoteException) {
      throw remoteException;
    } catch (Exception exception) {
      throw new UnexpectedException("undeclared checked exception", exception);
    } 
  }
  
  public void pauseBalls() throws RemoteException {
    try {
      this.ref.invoke(this, $method_pauseBalls_2, null, -7321077196845389229L);
    } catch (RuntimeException runtimeException) {
      throw runtimeException;
    } catch (RemoteException remoteException) {
      throw remoteException;
    } catch (Exception exception) {
      throw new UnexpectedException("undeclared checked exception", exception);
    } 
  }
}
