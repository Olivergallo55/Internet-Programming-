import java.io.*;
import java.net.*;

public class Client implements Runnable {
	
	private Server server;
	private Socket socket;
	
	public Client(Server server, Socket socket) {
		this.server = server;
		this.socket = socket; 
	}

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
		} catch (IOException e) {
			server.deleteClient(this); 
			e.printStackTrace();
		}	
	}
	
	public void sendMessage(String text) {
		try {
			PrintWriter pr = new PrintWriter(socket.getOutputStream(), true);
			pr.println(text);

		} catch (IOException e) {
			System.err.println("Failed to send the message : sendMassege");
		}
	} 
}
