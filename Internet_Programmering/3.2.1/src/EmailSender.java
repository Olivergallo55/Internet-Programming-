import java.awt.*;
import javax.swing.*;

public class EmailSender {
	private TextArea area;
	private TextField server = new TextField();
	private TextField userName = new TextField();
	private TextField password = new TextField();
	private TextField reciver = new TextField();
	private TextField sender = new TextField();
	private TextField subject = new TextField();
	private JButton button = new JButton("Send"); 

	public static void main(String[] args) {
		EmailSender mail = new EmailSender();
		mail.createGUI();
	}
	
	//TODO send crypted email
	
	//TODO password autanzitation

	private void createGUI() {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		area = new TextArea(); 
		panel.setLayout(new GridLayout(0, 2));
		panel.add(new JLabel("Server:"));
		panel.add(server);
		panel.add(new JLabel("Username:"));
		panel.add(userName);
		panel.add(new JLabel("Password:"));
		panel.add(password);
		panel.add(new JLabel("From:"));
		panel.add(reciver);
		panel.add(new JLabel("To:"));
		panel.add(sender);
		panel.add(new JLabel("Subject:"));
		panel.add(subject);
		button.setBackground(Color.lightGray);
		button.addActionListener(e->{
			clear();
		});

		JPanel down = new JPanel();
		frame.add(down);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Email sender");
		frame.getContentPane().add("North", panel);
		frame.getContentPane().add("Center", new JScrollPane(area));
		frame.getContentPane().add("South", button);
		frame.setSize(800, 500);
		frame.setVisible(true);

	}
	
	private void clear() {
		area.setText("");
		server.setText("");
		userName.setText("");
		password.setText("");
		reciver.setText("");
		sender.setText("");
		subject.setText("");
	}
}
