import java.awt.*;
import java.util.List;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;


/*
 * Server class that a client object can connect to. It hold all the connected clients as a client thread in an ArrayList. 
 * Inspiration comes from http://topman.com.np/blogs/10/how-i-create-chat-application-in-javafx-using-socket-programming
 */
public class Server implements AutoCloseable{ 
	public JTextArea textArea; 
	public Socket socket;
	private int port;
	private List<Client> clientList = Collections.synchronizedList(new ArrayList<>());  

	
	public static void main(String[] args) throws Exception {
		var start_port = args.length == 1 ? Integer.parseInt(args[0]) : 2000;
		try(Server s = new Server(start_port)){
			s.createGUI();
			s.beginServer(); 
		}
	}
	
	public Server(int port) {
		this.port = port; 
	}
	
	public void beginServer() throws IOException {
		boolean running = true;
		int i = 0; 
		try (ServerSocket serverSocket = new ServerSocket(port)){ //serversocket med host adressen
			
			while(running) {
				socket = serverSocket.accept();  
				Client client = new Client(this, socket);
				clientList.add(client);
				addClient();
				i++;
				
				Thread thread = new Thread(client);
				thread.start();
			}
			System.out.println("Amount of connected clients " + i);
		}
	}
	
	// Add client and broadcast to all clients
	private synchronized void addClient() {
		textArea.append("New client has connected " + socket.getInetAddress().getHostName() + '\n'); 
		if(clientList.size() >= 2)
		broadcast("New client has connected " + socket.getInetAddress().getHostName() + '\n');
	} 
	
	// Deletion of the client
		public synchronized void deleteClient(Client client) { 
			try {
				client.sendMessage(client + " Client discconected " + socket.getInetAddress().getHostName() + '\n');
				textArea.append(client + " Client discconected " + socket.getInetAddress().getHostName()+ '\n');
				clientList.remove(client);
			} catch (NullPointerException e) { 
				e.printStackTrace();
			}
		}
	
	//broadcasting
	public synchronized void broadcast(String text) {
		for(var i : clientList)
			i.sendMessage(text);
	}
		
	private void createGUI() {
		//create window and panels
		JFrame window = new JFrame("Server");
		
		//center pane
		JPanel center = new JPanel();
		textArea = new JTextArea(22,38);   
		center.add(textArea);
		
		//add the panes to the screen
		window.add(center, BorderLayout.CENTER);
		center.setBackground(Color.gray);
		
		//Miscellaneous
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setEditable(false); 
		window.setResizable(false);
		window.setSize(400, 400);
		window.setVisible(true);
	}

	//to fully close the socket
	@Override
	public void close() throws Exception {
		this.socket.close(); 
	}
}
