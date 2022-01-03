/**
 * @author Oliver Gallo, Gesällprov
 **/
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

	private Socket socket;
	private int port = 2005;
	private CreateXML xml;
	public Vector<String> users = new Vector<String>();
	public Vector<Connection> connections = new Vector<Connection>();

	public static void main(String[] args) {
		new Server(); 
	}

	//New server
	public Server() {
		xml = new CreateXML(this);
		connect();
	}
	/*
	 * Connects the clients to the server
	 */
	private void connect() {
		ServerSocket server_socket = null;
		try {
			server_socket = new ServerSocket(port);

			while (true) {
				socket = server_socket.accept();
				xml.sendXML(xml.createXML("", "", "New client joined the conversation"));
				Connection con = new Connection(this, socket, xml);
				connections.add(con);
				Thread t = new Thread(con);
				t.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (server_socket != null)
					server_socket.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Disconnects the user
	 * 
	 * @param user is the user
	 */
	public synchronized void disconnectUser(String user) {
		users.remove(user);
	}

	/*
	 * Broadcasts the message
	 * 
	 * @param text is the broadcasted text
	 */
	public synchronized void broadcast(String text) {
		connections.forEach((n) -> n.sendMessage(text));
	}
}
