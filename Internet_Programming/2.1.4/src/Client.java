
/*
 * Assignment 2.1.4 for IPROG course, about socket and image sending 
 * @author Oliver Gallo olga7031
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class Client {

	public static Color myWhite = new Color(255, 255, 255);
	public Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private JTextPane textArea;

	public static void main(String[] args) throws IOException {
		Client client = new Client();
		client.createGUI();
		client.startConnection("atlas.dsv.su.se", 4848);
	}

	/*
	 * Starts connection
	 * 
	 * @ip is the host address
	 * 
	 * @port is the network port number
	 */
	private void startConnection(String ip, int port) {
		try {
			socket = new Socket(ip, port);
			out = new ObjectOutputStream(socket.getOutputStream());

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							in = new ObjectInputStream(socket.getInputStream());
							ImageIcon n = (ImageIcon) in.readObject();
							System.out.println(n);
							messageReciver(n);
							if (n.equals(null)) {
								System.out.println("Client disconnected from server");
								socket.close();
								break;
							}
						} catch (IOException | ClassNotFoundException e) {
							System.err.print("Error when reading line");
							System.exit(0);
							break;
						}
					}
				}

			});
			t.start();

		} catch (UnknownHostException e) {
			System.err.println("unknownhostexxception");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("ioexception");
			e.printStackTrace();
		}
	}

	/*
	 * Receive message
	 * 
	 * @param text is the image to be sent
	 */
	public void messageReciver(ImageIcon text) {
		textArea.insertIcon(text);
	}

	/*
	 * Creates GUI
	 */
	private void createGUI() {
		// create window and panels
		JFrame window = new JFrame("Chatt app");

		// center pane
		JPanel center = new JPanel(new BorderLayout());
		textArea = new JTextPane();
		center.add(textArea);

		// down pane
		JPanel down = new JPanel();
		JTextField textField = new JTextField(32);
		JButton button = new JButton("Send");
		button.addActionListener(e -> {
			try {
				ImageIcon file = new ImageIcon("C:/Users/Admin/Desktop/Internet_Programming/2.1.4/resource/budapest.jpg"); 
				out.writeObject(file);

			} catch (IOException e2) {
				e2.printStackTrace();
			}
		});

		down.add(textField);
		down.add(button);

		// add the panes to the screen
		window.add(center, BorderLayout.CENTER);
		window.add(down, BorderLayout.SOUTH);

		// Miscellaneous
		center.setBackground(Color.GRAY);
		textField.setPreferredSize(new Dimension(35, 30));
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
