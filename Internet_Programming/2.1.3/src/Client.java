/*
 * Assignment 2.1.3 for IPROG course, about stream sockets and XML 
 * @author Oliver Gallo olga7031
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

	private Socket socket = null;
	private BufferedReader bufferReader = null;
	public PrintWriter printWriter = null;
	private String host = "atlas.dsv.su.se";
	private int port = 9494;
	private XMLSender xml;


	public static void main(String[] args) {
		 new Client();   
	}
	
	public Client() {
		connectToServer();
		xml = new XMLSender(this);
		new GUI(xml); 
	}

	/*
	 * Connects to the server and creates a new thread
	 */
	private void connectToServer() {
		try {
			socket = new Socket(host, port); 
			bufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWriter = new PrintWriter(socket.getOutputStream(), true);

			System.out.println(
					"Connected to: <" + socket.getInetAddress().toString() + ">" + " " + "<" + socket.getPort() + ">");
			Thread t = new Thread(this);  
			t.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Runs the thread and receives the XML data
	 */
	@Override
	public void run() {

		while (true) { 
			try {
				String n = bufferReader.readLine();
				xml.reciveXML(n); 
				if (n.equals("exit") || n.equals(null)) {
					System.out.println("Client disconnected from server");
					System.exit(1);
					socket.close();
					break;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Sends message
	 * 
	 * @param text is the sended text
	 */
	public void sendMessage(String text) {
		printWriter.println(text);
	}
}
