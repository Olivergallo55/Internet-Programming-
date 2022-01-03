import java.awt.BorderLayout;
import javax.swing.JFrame;

/*
 * Assignment 2.2.1 for IPROG course, about datagram socket with unicast 
 * @author Oliver Gallo olga7031
 * Insperation comes from https://www.baeldung.com/udp-in-java tutorial for UDP connections in java 
 */


public class Draw extends JFrame {
	private Paper paper;

	public static void main(String[] args) {
		new Draw(args);
	}

	/*
	 * Starts the app on the given port
	 */
	public Draw(String[] args) {
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
		paper = new Paper(args);
		getContentPane().add(paper, BorderLayout.CENTER);

		setSize(640, 480);
		setVisible(true);
	}
}
