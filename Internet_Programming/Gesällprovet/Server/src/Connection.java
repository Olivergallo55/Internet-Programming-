/**
 * @author Oliver Gallo, Gesällprov
 **/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection implements Runnable {

	private Socket socket;
	private Server server;
	private CreateXML xml;
	private String user;

	public Connection(Server server, Socket socket, CreateXML xml) {
		this.server = server;
		this.socket = socket;
		this.xml = xml;
	}

	/**
	 * Runs a new thread with a new user
	 **/
	@Override
	public void run() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while ((user = reader.readLine()) != null) {
				xml.reciveXML(user);

				if (!server.users.contains(xml.getSender(user)) && !server.users.contains(xml.getReceiver(user))) {
					server.users.add(xml.getSender(user));
					server.users.add(xml.getReceiver(user));
					System.out.println(server.users);
				}

				if (xml.getMessage(user).equals("exit")) {
					server.disconnectUser(xml.getSender(user));
					server.connections.remove(this);
					xml.sendXML(xml.createXML(xml.getSender(user), "", " disconnected from the server"));
					break;
				}

				xml.sendXML(xml.createXML(xml.getSender(user), "", xml.getMessage(user)));
			}

		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
	}

	/*
	 * Sends the message
	 * 
	 * @param text is the message to be sent
	 */
	public synchronized void sendMessage(String text) {
		try {
			PrintWriter pr = new PrintWriter(socket.getOutputStream(), true);
			pr.println(text);

		} catch (IOException e) {
			System.err.println("Failed to send the message : sendMassege");
		}
	}
}
