/*
 * Assignment 2.1.3 for IPROG course, about stream sockets and XML 
 * @author Oliver Gallo olga7031
 */
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.jdom2.Document;

public class GUI {

	public static JTextArea area;
	private JTextField nameField = new JTextField("oliver");
	private JTextField emailField = new JTextField("oliver@gmail.se");
	private JTextField homepageField = new JTextField("http://dsv.su.se/~oliver");
	private JTextField messageField = new JTextField("hello!");
	private JButton button;
	private XMLSender xml;

	public GUI(XMLSender xml) {
		this.xml = xml;
		createGUI();
	}

	/*
	 * Creates the GUI
	 */
	private void createGUI() {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		area = new JTextArea();
		panel.setLayout(new GridLayout(5, 2));
		panel.add(new JLabel("Name:"));
		panel.add(nameField);
		panel.add(new JLabel("Email:"));
		panel.add(emailField);
		panel.add(new JLabel("Homepage:"));
		panel.add(homepageField);
		panel.add(new JLabel("Message:"));
		panel.add(messageField);
		panel.add(new JLabel("Send:"));
		button = new JButton("Send");
		button.addActionListener(e -> {
			String msg = messageField.getText();
			messageField.setText("");
			Document d = xml.createXML(nameField.getText(), emailField.getText(), homepageField.getText(), msg);
			xml.sendXML(d);
		});
		panel.add(button);
		button.setBackground(Color.lightGray);

		JPanel down = new JPanel();
		frame.add(down);

		area.setEditable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("XML APP");
		frame.getContentPane().add("Center", new JScrollPane(area));
		frame.getContentPane().add("South", panel);
		frame.setSize(800, 500);
		frame.setVisible(true);
	}

}
