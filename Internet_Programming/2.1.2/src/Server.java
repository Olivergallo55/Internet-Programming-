/*
 * Assignment 2.1.2 for IPROG course, about stream sockets on the server side 
 * @author Oliver Gallo olga7031 
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;


/*
 * Server class that a client object can connect to. It hold all the connected clients as a client thread in an ArrayList. 
 * Inspiration comes from http://topman.com.np/blogs/10/how-i-create-chat-application-in-javafx-using-socket-programming
 */
public class Server implements AutoCloseable {
	public JTextArea textArea;
	public Socket socket;
	private int port;
	private JFrame window;
	private List<Client> clientList = Collections.synchronizedList(new ArrayList<>());

	public static void main(String[] args) throws Exception {
		var start_port = args.length == 1 ? Integer.parseInt(args[0]) : 2000;
		try (Server s = new Server(start_port)) {
			s.createGUI();
			s.beginServer();
		}
	}

	public Server(int port) {
		this.port = port;
	}

	/*
	 * Begins the server, waits for client connection
	 * 
	 * @throws IOException
	 */
	public void beginServer() throws IOException {
		boolean running = true;
		int i = 0;
		try (ServerSocket serverSocket = new ServerSocket(port)) { // serversocket med host adressen

			while (running) {
				socket = serverSocket.accept();
				setTitle(0);
				Client client = new Client(this, socket);
				addClient(client);
				i++;

				Thread thread = new Thread(client);
				thread.start();
			}
			System.out.println("Amount of connected clients " + i);
		}
	}

	/*
	 * Adds a new client to the server and broadcast it
	 */
	private synchronized void addClient(Client c) {
		clientList.add(c);
		setTitle(clientList.size());
		textArea.append("New client has connected " + socket.getInetAddress().getHostName() + '\n');
		if (clientList.size() >= 2)
			broadcast("New client has connected " + socket.getInetAddress().getHostName() + '\n');
	}

	/*
	 * Deletes the client and broadcast it to all connected clients
	 * 
	 * @param client is the deleted client
	 */ public synchronized void deleteClient(Client client) {
		try {
			clientList.remove(client);
			setTitle(clientList.size());
			broadcast("Client discconected " + socket.getInetAddress().getHostName() + '\n');
			textArea.append(client + " Client discconected " + socket.getInetAddress().getHostName() + '\n');
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	private void setTitle(int amount) {
		window.setTitle("SERVER ON: " + socket.getInetAddress().getHostName() + " PORT: " + port
				+ " AMOUNT OF CLIENTS: " + amount);
	}

	/*
	 * Broadcasts message for every client connected to the server
	 * 
	 * @param text is the broadcasted message
	 */
	public synchronized void broadcast(String text) {
		for (var i : clientList)
			i.sendMessage(text);
	}

	/*
	 * Creates GUI for the application
	 */
	private void createGUI() {
		// create window and panels
		window = new JFrame();

		// center pane
		JPanel center = new JPanel();
		textArea = new JTextArea(22, 38);
		center.add(textArea);

		// add the panes to the screen
		window.add(center, BorderLayout.CENTER);
		center.setBackground(Color.gray);

		// Miscellaneous
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setEditable(false);
		window.setResizable(false);
		window.setSize(400, 400);
		window.setVisible(true);
	}

	// to fully close the socket
	@Override
	public void close() throws Exception {
		this.socket.close();
	}
}
