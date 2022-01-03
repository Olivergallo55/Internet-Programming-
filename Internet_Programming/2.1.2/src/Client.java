import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * Assignment 2.1.2 for IPROG course, about stream sockets on the server side 
 * @author Oliver Gallo olga7031 
 */


public class Client implements Runnable {

	private Server server;
	private Socket socket;

	public Client(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
	}

	/*
	 * Run the server each client have a unique connection to the server with its
	 * own thread this method runs that thread
	 */
	@Override
	public void run() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
			server.deleteClient(this);
			System.err.println(e1 + " : Client run method");
			return;
		}

		String n;
		try {
			while ((n = reader.readLine()) != null) {
				System.out.println(n);
				if (n.equals("exit")) {
					server.deleteClient(this);
					System.out.println("Disconnected client from the server");
					break;
				}
				server.broadcast(n);
				server.textArea.append(n + '\n');
			}
			server.deleteClient(this);
		} catch (IOException e) {
			server.deleteClient(this);
			e.printStackTrace();
		}
	}

	/*
	 * Sends in order to display it
	 * 
	 * @param text is the sended text
	 */
	public void sendMessage(String text) {
		try {
			PrintWriter pr = new PrintWriter(socket.getOutputStream(), true);
			pr.println(text);

		} catch (IOException e) {
			System.err.println("Failed to send the message : sendMassege");
		}
	}
}
