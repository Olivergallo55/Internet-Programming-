import java.io.*;
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

	@Override
	public void run() {

		while (true) { 
			try {
				String n = bufferReader.readLine();
				//System.out.println("\n\n\n" + "Det här är utskriften jag får: " + n + "\n\n\n" ); 
				xml.reciveXML(n); 
				//GUI.area.append(n + '\n'); 
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
	
	public void sendMessage(String text) {
		printWriter.println(text);
	}
}
