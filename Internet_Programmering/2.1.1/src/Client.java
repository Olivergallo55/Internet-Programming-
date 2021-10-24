import java.io.BufferedReader;
import java.io.IOException;

//create a new client thread
public class Client implements Runnable{

	private Reciver reciver;
	private BufferedReader buffer;
	
	public Client(Reciver reciver,BufferedReader buffer) {
		this.reciver = reciver;
		this.buffer = buffer;
	}
	
	/*
	 * Continuously updates the messages while the loop is not broken. 
	 * The loop breaks when the client writes exit or when the BufferedReader returns null. 
	 * @catches IOException it brakes the loop and exits the application.  
	 */
	@Override
	public void run() {
		while (true) {
			try {
				String n = buffer.readLine();
				System.out.println(n);
				if (n.equals("exit") || n.equals(null)) {
					System.out.println("Client disconnected from server");
					reciver.socket.close();
					break;
				}
				reciver.messageReciver(n);
			} catch (IOException e) {
				System.err.print("Error when reading line");
				System.exit(0);
				break;
			}
		}
	}

}
