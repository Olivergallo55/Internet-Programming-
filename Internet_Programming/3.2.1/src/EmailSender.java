
/* @author Oliver Gallo 
 * Assignment 3.2.1
 * Inspiration comes from https://javaee.github.io/javamail/docs/api/com/sun/mail/smtp/package-summary.html 
 */
import java.awt.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;

public class EmailSender extends Authenticator {
	private TextArea area;
	private TextField server = new TextField();
	private TextField userName = new TextField();
	private TextField password = new TextField();
	private TextField reciver = new TextField();
	private TextField sender = new TextField();
	private TextField subject = new TextField();
	private JButton button = new JButton("Send");

	public static void main(String[] args) {
		new EmailSender();
	}

	public EmailSender() {
		createGUI();
	}

	/*
	 * Sends email
	 */
	private void sendEmail() {
		Properties p = new Properties();
		p.put("mail.smtp.host", server.getText());
		p.put("mail.smtp.port", "465");
		p.put("mail.smtp.auth", "true");
		p.put("mail.smtp.serverFactory.port", "465");
		p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		p.put("mail.smtp.socketFactory.fallback", "false");

		Session ses = Session.getDefaultInstance(p, autenticator());

		try {//Compose the email
			MimeMessage mime = new MimeMessage(ses);
			mime.setFrom(new InternetAddress(reciver.getText()));
			mime.addRecipient(Message.RecipientType.TO, new InternetAddress(sender.getText()));
			mime.setSubject(subject.getText());
			mime.setText(area.getText());

			Transport.send(mime);//send the email
			System.out.println("Message is now sent");//debug

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Authenticates
	 */
	private Authenticator autenticator() {

		Authenticator au = new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName.getText(), password.getText());
			}
		};
		return au;
	}

	/*
	 * Creates the GUI
	 */
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
		button.addActionListener(e -> {
			sendEmail();
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
