/**
 * @author Oliver Gallo, Gesällprov
 **/
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;


public class GUI {
	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
	private static final int HEIGHT = 400, WEIDTH = 300;
	public ArrayList<JPanel> panels = new ArrayList<JPanel>();
	public String searchedName;
	private JButton changePassword_Button;
	private JButton createNewUser_Button;
	private JButton deleteUser_Button;
	private JButton searchUser_Button;
	private JButton logout_Button;
	private JButton login_Button;
	private JTextField login_usernameField;
	private JTextField create_newUserField;
	private JTextField search_usernameField;
	private JTextField update_passwordField;
	private JPasswordField login_passwordField;
	private JPasswordField create_newPasswordField;
	public JPanel panel = null;
	public JFrame frame;
	private int count = 0;
	private DatabaseConnection db;

	public GUI() {
		createGUI();
	}

	/**
	 * Method to initialize all the used objects.
	 **/
	private void initalizeVariables() {
		frame = new JFrame();
		login_Button = new JButton("Log in");
		logout_Button = new JButton("Log out");
		createNewUser_Button = new JButton("Create new User");
		searchUser_Button = new JButton("Start chat");
		deleteUser_Button = new JButton("Delete account");
		changePassword_Button = new JButton("Change Password");
		update_passwordField = new JTextField();
		db = new DatabaseConnection(this);
		login_usernameField = new JTextField();
		login_passwordField = new JPasswordField();
		login_passwordField.setEchoChar('*');
		create_newUserField = new JTextField();
		create_newPasswordField = new JPasswordField();
		search_usernameField = new JTextField();
	}

	/**
	 * Method to draw the user interface
	 **/
	private void createGUI() {
		initalizeVariables();
		panel = createPanel1();
		buttonHandlers();
		frame.setTitle("Login Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(HEIGHT, WEIDTH);
		frame.setVisible(true);
	}

	/**
	 * Method handles all the button actions
	 **/
	private void buttonHandlers() {
		loginButtonHandler();
		logoutButtonHandler();
		createNewUserButtonHandler();
		searchUserHandler();
		deleteUserHandler();
		updatePasswordHandler();
	}

	/**
	 * Method creates a new user, checks if the user name already exists and
	 * validates that the password holds a certain pattern
	 **/
	private void createNewUserButtonHandler() {
		createNewUser_Button.addActionListener(e -> {

			createNewUserWindow();

			if (!create_newUserField.getText().isBlank() && create_newPasswordField.getPassword().length != 0) {
				if (isValid(String.valueOf(create_newPasswordField.getPassword()))) {
					try {
						db.createNewUser(create_newUserField.getText(),
								String.valueOf(create_newPasswordField.getPassword()));
						create_newUserField.setText("");
						create_newPasswordField.setText("");
					} catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e1) {
						create_newUserField.setText("");
						create_newPasswordField.setText("");
						JOptionPane.showMessageDialog(null, "User already exist");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Password must contain at least one digit [0-9].\r\n"
							+ "Password must contain at least one lowercase Latin character [a-z].\r\n"
							+ "Password must contain at least one uppercase Latin character [A-Z].\r\n"
							+ "Password must contain at least one special character like ! @ # & ( ).\r\n"
							+ "Password must contain a length of at least 8 characters and a maximum of 20 characters.");
				}
			} else {
				JOptionPane.showMessageDialog(null, "Username or password field is empty");
			}
		});
	}

	/**
	 * Method handles the log out button
	 **/
	private void logoutButtonHandler() {

		logout_Button.addActionListener(e -> {
			changeToWindow1();
		});
	}

	/**
	 * Method handles user check and password validation If any of these are
	 * incorrect an error message will appear
	 */
	private void loginButtonHandler() {

		login_Button.addActionListener(e -> {

			if (db.checkUser(login_usernameField.getText())) {
				if (db.validatePassword(login_passwordField.getPassword(), login_usernameField.getText())) {
					changeToWindow2();
				} else {
					JOptionPane.showMessageDialog(null, "Wrong username or password");
					login_passwordField.setText("");
					login_usernameField.setText("");
				}
			} else {
				JOptionPane.showMessageDialog(null, "User dont exist");
				login_passwordField.setText("");
				login_usernameField.setText("");
			}
		}); 
	}

	/**
	 * Method search for a specific user //TODO check if the searched user exists in
	 * the database if not the user is not using the app
	 */
	private void searchUserHandler() {
		searchUser_Button.addActionListener(x -> {
			searchUserPanel();

			if (db.checkUser(search_usernameField.getText())) {
				if (!search_usernameField.getText().equals(login_usernameField.getText())) {
					new User(login_usernameField.getText(), this);
					searchedName = search_usernameField.getText();
				} else
					JOptionPane.showMessageDialog(null, "Cant send message to yourself", "Failure",
							JOptionPane.ERROR_MESSAGE);

			} else {
				JOptionPane.showMessageDialog(null, "User dont exist", "Failure", JOptionPane.ERROR_MESSAGE);
				search_usernameField.setText("");
			}
		});
	}

	/**
	 * Paints the search panel
	 */
	private void searchUserPanel() {

		JPanel main = new JPanel(new BorderLayout(5, 5));

		JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
		labels.add(new JLabel("Enter the Username:", SwingConstants.TRAILING));
		labels.add(search_usernameField);
		main.add(labels, BorderLayout.LINE_START);

		UIManager.put("OptionPane.minimumSize", new Dimension(350, 80));
		JOptionPane.showMessageDialog(frame, main, "Search for a user", JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * Method deletes a specific user
	 */
	private void deleteUserHandler() {
		deleteUser_Button.addActionListener(x -> {

			int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account?", "Remove",
					JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (option == 0) {
				db.deleteUser(login_usernameField.getText());
				JOptionPane.showMessageDialog(null, "Your account have been deleted", "Deleted",
						JOptionPane.ERROR_MESSAGE);
				changeToWindow1();
			}

		});
	}

	/**
	 * Method updates the users password in the database. This is done after the
	 * user is logged in which limits the change of other users passwords.
	 **/
	private void updatePasswordHandler() {
		changePassword_Button.addActionListener(e -> {
			updatePasswordPanel();

			if (isValid(update_passwordField.getText())) {
				db.updatePassword(update_passwordField.getText(), login_usernameField.getText());
				JOptionPane.showMessageDialog(null, "Your password have been updated");
			} else {
				JOptionPane.showMessageDialog(null, "Password must contain at least one digit [0-9].\r\n"
						+ "Password must contain at least one lowercase Latin character [a-z].\r\n"
						+ "Password must contain at least one uppercase Latin character [A-Z].\r\n"
						+ "Password must contain at least one special character like ! @ # & ( ).\r\n"
						+ "Password must contain a length of at least 8 characters and a maximum of 20 characters.",
						"Failed", JOptionPane.ERROR_MESSAGE);
			}
		});

	}

	/**
	 * Paints the update password panel
	 **/
	private void updatePasswordPanel() {

		JPanel main = new JPanel(new BorderLayout(5, 5));

		JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
		labels.add(new JLabel("Enter the new Password:", SwingConstants.TRAILING));
		labels.add(update_passwordField);
		main.add(labels, BorderLayout.LINE_START);

		UIManager.put("OptionPane.minimumSize", new Dimension(350, 80));
		JOptionPane.showMessageDialog(frame, main, "Search for a user", JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * Changes back to starting scene, when the user logs out
	 **/
	private void changeToWindow1() {
		clearWindow();
		panel = createPanel1();
		repaintWindow();
		panel.setVisible(true);
		login_usernameField.setText("");
		login_passwordField.setText("");
	}

	/**
	 * Changes to a new scene, when the login was successful
	 **/
	public void changeToWindow2() {
		clearWindow();
		panel = createPanel2();
		repaintWindow();
		panel.setVisible(true);
	}

	/**
	 * Method creates the starting scene
	 **/
	private JPanel createPanel1() {
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(4, 2, 4, 4));
		panel1.add(new JLabel("User name"));
		panel1.add(new JLabel("Password"));
		panel1.add(login_usernameField);
		panel1.add(login_passwordField);
		panel1.add(login_Button);
		panel1.add(createNewUser_Button);
		JButton showPasswordButton = new JButton("Show Password");
		showPasswordButton.setBounds(300, 300, 50, 50);
		showPasswordButton.addActionListener(e -> {
			showPassword();
		});
		panel1.add(showPasswordButton);
		frame.add(panel1, BorderLayout.NORTH);
		panels.add(panel1);
		return panel1;
	}

	/**
	 * Method creates a new scene, when the login was successful
	 **/
	private JPanel createPanel2() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		JPanel layout = new JPanel(new GridLayout(0, 1, 2, 2));
		JPanel up = new JPanel();
		up.add(new JLabel("Login successful! Welcome " + login_usernameField.getText()));
		layout.add(changePassword_Button);
		layout.add(deleteUser_Button);
		layout.add(searchUser_Button);
		layout.add(logout_Button);
		panel.add(up, BorderLayout.NORTH);
		panel.add(layout);
		frame.add(panel);
		panels.add(panel);
		return panel;
	}

	/**
	 * Changes to a new scene, to register new user
	 **/
	private void createNewUserWindow() {
		JPanel main = new JPanel(new BorderLayout(5, 5));

		JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
		labels.add(new JLabel("Username", SwingConstants.TRAILING));
		labels.add(new JLabel("Password", SwingConstants.TRAILING));
		main.add(labels, BorderLayout.LINE_START);

		JPanel textFields = new JPanel(new GridLayout(0, 1, 2, 2));
		textFields.add(create_newUserField);
		textFields.add(create_newPasswordField);
		main.add(textFields, BorderLayout.CENTER);

		UIManager.put("OptionPane.minimumSize", new Dimension(350, 80));
		JOptionPane.showMessageDialog(frame, main, "Create a new user", JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * Method checks if the number is even or uneven
	 */
	private boolean isEven(int count) {
		return count % 2 == 0 ? true : false;
	}

	/**
	 * Method shows the password on every even click and hides it every uneven click
	 */
	private void showPassword() {
		if (isEven(count)) {
			login_passwordField.setEchoChar((char) 0);
			count++;
		} else {
			login_passwordField.setEchoChar('*');
			count++;
		}
	}

	/**
	 * Method clears the window in order to be able to repaint it
	 */
	public void clearWindow() {
		for (JPanel c : panels)
			frame.getContentPane().remove(c);
	}

	/**
	 * Method repaints the window
	 */
	public void repaintWindow() {
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
	}

	/**
	 * Method checks if the password contains minimum 8 digits and maximum 20, at
	 * least one letter, one Latin character, one special sign and at least one
	 * upper case letter. This is done with REGEX.
	 * 
	 * @param password is the users password input
	 **/
	private boolean isValid(String password) {
		final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}
}
