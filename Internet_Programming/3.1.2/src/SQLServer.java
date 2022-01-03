
/*
 * Assignment 3.1.2 for IPROG course, about database connections 
 * @author Oliver Gallo olga7031
 */

import java.awt.Button;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class SQLServer {
	private TextArea area = new TextArea();
	private TextField nameField = new TextField();
	private TextField emailField = new TextField();
	private TextField pageField = new TextField();
	private TextField commentField = new TextField();
	private Button button = new Button("Add to database");
	private Statement statement = null;
	private Connection dbConnection;

	private String computer = "atlas.dsv.su.se";
	private String databaseName = "db_20243415";
	private String userName = "usr_20243415";
	private String password = "243415";
	private String url = "jdbc:mysql://" + computer + "/" + databaseName;

	public static void main(String[] args) throws SQLException {
		SQLServer s = new SQLServer();
		s.createGUI();
		s.connectDatabse();
		s.showComments();
	}

	/*
	 * Connects the database
	 */
	private void connectDatabse() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			dbConnection = DriverManager.getConnection(url, userName, password);
			statement = (Statement) dbConnection.createStatement();
			System.out.println("connection succesfull");
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("Couldnt connect to the sql database" + e);
		}
	}

	/*
	 * Adds the content to the database
	 * 
	 * @param name is the given name
	 * 
	 * @param email is the given email
	 * 
	 * @param webbpage is the given webbpage
	 * 
	 * @param comment is the given comment
	 */
	private void addComment(String name, String email, String webbpage, String comment) throws SQLException {
		PreparedStatement st = null;
		try {
			String insert = "INSERT INTO guestbook (name, email, webbpage, comment) VALUES('" + name + "', '" + email
					+ "', '" + webbpage + "', '" + comment + "')";
			st = (PreparedStatement) dbConnection.prepareStatement(insert);
			st.executeUpdate(insert);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			st.close();
		}
	}

	/*
	 * Show the comments
	 */
	private void showComments() throws SQLException {
		area.setText(null);
		String result = "SELECT * FROM guestbook WHERE name !='' AND name IS NOT NULL";
		ResultSet set = (ResultSet) statement.executeQuery(result);
		while (set.next()) {
			area.append("Name: " + set.getString(1) + "\n");
			area.append("Email: " + set.getString(2) + " ");
			area.append("Webbpage: " + set.getString(3) + "\n");
			area.append("Comment: " + set.getString(4));
			area.append("\n\n");
		}
	}

	/*
	 * Censurs HTML code, Inspiration from
	 * https://www.javamex.com/tutorials/regular_expressions/search_replace_loop.
	 * shtml
	 * 
	 */
	private String censor(String text) {
		Pattern patter = Pattern.compile("<.*>");
		Matcher match = patter.matcher(text);
		return match.replaceAll("censur");
	}

	/*
	 * Creates the GUI
	 */
	private void createGUI() {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		panel.add(new Label("Name:"));
		panel.add(nameField);
		panel.add(new Label("Email:"));
		panel.add(emailField);
		panel.add(new Label("Homepage:"));
		panel.add(pageField);
		panel.add(new Label("Comments:"));
		panel.add(commentField);
		panel.add(new Label("Add:"));
		panel.add(button);
		button.setBackground(Color.lightGray);
		button.addActionListener(e -> {
			try {
				String name = censor(nameField.getText());
				String email = censor(emailField.getText());
				String webbpage = censor(pageField.getText());
				String comment = censor(commentField.getText());

				if (!name.equals(""))
					addComment(name, email, webbpage, comment);
				showComments();
				nameField.setText("");
				commentField.setText("");
				emailField.setText("");
				pageField.setText("");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});

		JPanel down = new JPanel();
		frame.add(down);
		area.setEditable(false);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("database app");
		frame.getContentPane().add("North", panel);
		frame.getContentPane().add("Center", new JScrollPane(area));
		frame.setSize(800, 500);
		frame.setVisible(true);
	}
}
