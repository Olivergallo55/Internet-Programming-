/**
 * @author Oliver Gallo, Gesällprov
 **/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

public class User {

	public static String userName;
	private int port = 2005; 
	private String host = "localhost"; 
	public Socket socket;
	public static PrintWriter writer;
	private XMLSender xml;
	
	//Creates a new user and connection to the server
	public User(String userName, String receiver, String msg) {
		User.userName = userName;
		xml = new XMLSender(userName, receiver, msg);
		startConnection();
	}

	public User(String userName, GUI gui) {
		User.userName = userName;
		xml = new XMLSender();
		new ChatScene(xml, gui);
		startConnection();
	}

	/*
	 * sends the message
	 * @param text writes the text
	 */
	public static void sendMessage(String text) {
		writer.println(text);
	}

	/*
	 * Returns the user name 
	 */
	public String getUsername() {
		return userName;
	}

	/*
	 * Starts the connection between the client and the server 
	 */
	private void startConnection() {
		try {
			Socket socket = new Socket(host, port);
			writer = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					updateMassege(buffer, socket);
				}
			});
			t.start();

		} catch (IOException e) {
			e.printStackTrace();
			ChatScene.area.append("Could not connect to the server");
			JOptionPane.showMessageDialog(null, "Could not connect to the server");
		}
	}

	/*
	 * Appends the message 
	 */
	private void receiveMessage(String text) {
		ChatScene.area.append("  " + text + "\n");
	}

	/*
	 * Continuously listens to the socket and updates the message sent to the server 
	 */
	private void updateMassege(BufferedReader buffer, Socket socket) {
		while (true) {
			try {
				String message = buffer.readLine();
				xml.reciveXML(message); 
				if (message.equals("exit") || message.equals(null)) {
					receiveMessage(userName + " disconnected");
					socket.close();
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
				break;
			}
		}
	}

}
