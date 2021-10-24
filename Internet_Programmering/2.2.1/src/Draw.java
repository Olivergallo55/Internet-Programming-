/*
 * Insperation comes from https://www.baeldung.com/udp-in-java tutorial for UDP connections in java 
 */

import java.awt.*;
import javax.swing.*;

public class Draw extends JFrame {
	private Paper paper;

	public static void main(String[] args) {
		new Draw(args);
	}

	public Draw(String[] args) {
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
		paper = new Paper(args);
		getContentPane().add(paper, BorderLayout.CENTER);

		setSize(640, 480);
		setVisible(true);
	}
}
