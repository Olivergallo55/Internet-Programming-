import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*; 

public class Reciver {
	
	public static Color myWhite = new Color(255,255,255);
	private PrintWriter writer;
	public Socket socket;
	private BufferedReader buffer;
	private JTextArea textArea; 

	public static void main(String[] args) { 
		Reciver reciver = new Reciver();
		reciver.createGUI();
		reciver.startConnection("127.0.0.1", 2001); 
	}
	
	private void startConnection(String ip, int port) {
		try {
			socket = new Socket(ip, port);
			writer = new PrintWriter(socket.getOutputStream(), true);
			buffer = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
			
			messageReciver("Connected to: <" + socket.getInetAddress().toString() + ">" + " " + "<" + socket.getPort() + ">"); 

			Client client = new Client(this, buffer);
			Thread t = new Thread(client);
			t.start(); 
			
		} catch (UnknownHostException e) {
			System.err.println("unknownhostexxception");
			e.printStackTrace();
		} catch (IOException e) { 
			System.err.println("ioexception");
			e.printStackTrace();
		}
	}
	
	public void messageReciver(String text) {
		textArea.append(text + '\n');
	}
	
	private void createGUI() {
		//create window and panels
		JFrame window = new JFrame("Chatt app");
		
		//center pane
		JPanel center = new JPanel();
		textArea = new JTextArea(23,40);   
		center.add(textArea);
		
		//down pane
		JPanel down = new JPanel(); 
		JTextField textField = new JTextField(32);
		JButton button = new JButton("Send");
		button.addActionListener(e->{
			String message = textField.getText();
			//textArea.append(message + '\n');
			textField.setText(""); 
			writer.println(message);
		});
		
		
		textField.addActionListener(e->{
			String message = textField.getText();
			textField.setText(""); 
			writer.println(message); 
		});
		down.add(textField);
		down.add(button);
		
		//add the panes to the screen
		window.add(center, BorderLayout.CENTER);
		window.add(down, BorderLayout.SOUTH);
		
		//Miscellaneous
		center.setBackground(Color.GRAY);
		textField.setPreferredSize(new Dimension(35,30));
		textArea.setEditable(false); 
		down.setBackground(Color.gray);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		textArea.setBackground(Color.LIGHT_GRAY);
		textField.setBackground(myWhite); 
		window.setResizable(false);
		window.setSize(400, 400);
		window.setVisible(true);
	}

}
