/**
 * @author Oliver Gallo, Gesällprov
 **/
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.jdom2.Document;


public class ChatScene {
	public static JTextArea area;
	private XMLSender sender; 
	private GUI gui; 

	public ChatScene(XMLSender sender, GUI gui) {
		this.sender = sender;
		this.gui = gui; 
		showScene();
	}
	
	// Shows the new window
	private void showScene() {
		gui.clearWindow();
		gui.panel = createScene(gui.frame, gui.panels);
		gui.repaintWindow();
		gui.panel.setVisible(true);
	}

	// Creates the new Scene
	public JPanel createScene(JFrame frame, List<JPanel> panels) {
		JPanel center = new JPanel(new BorderLayout(5, 5));
		area = new JTextArea();
		area.setEditable(false);
		area.setLineWrap(true);
		area.setWrapStyleWord(true); 
		center.add(area, BorderLayout.CENTER);
		
		JPanel down = new JPanel();
		JTextField textField = new JTextField(26);
		JButton send_button = new JButton("Send"); 
		send_button.addActionListener(e -> {
			Document d = sender.createXML(User.userName, gui.searchedName, textField.getText());
			sender.sendXML(d);
			textField.setText("");
		});
		JButton goback_button = new JButton("Back");
		goback_button.addActionListener(e -> {
			gui.changeToWindow2();
		});

		down.add(goback_button);
		down.add(textField);
		down.add(send_button);

		center.add(down, BorderLayout.SOUTH);

		frame.add(center, BorderLayout.CENTER);
		panels.add(center);
		return center;
	}

}
