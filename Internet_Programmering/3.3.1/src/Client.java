import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Client implements Runnable {
	private RemoteServer remote = null;
	private int xLim = 500, yLim = 500, rMax = 70;
	private JPanel frame;
	private Vector<Object> ballsRaw = new Vector<Object>();

	public static void main(String[] args) {
		Client c = new Client();
		c.createGUI();
		c.findRMI(args[0]);
		Thread t = new Thread(c);
		t.start();
	}

	private void createGUI() {
		// create window and panels
		JFrame window = new JFrame("Boll app");
		// Panel for the graphics
		frame = new JPanel();
		keyBoard(window);

		window.getContentPane().add(frame);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setSize(xLim, yLim);
		window.setVisible(true);

	}

	public void keyBoard(JFrame frame) {
		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '+') {
					System.out.println("hello");
					try {
						remote.addBall();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				} else if (e.getKeyChar() == 'p') {
					try {
						remote.pauseBalls();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}

		});
	}
	
	private void findRMI(String rmi) {
		String url = "rmi://" + rmi + "/";
		try {
			remote = (RemoteServer) Naming.lookup(url + "server");
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void run() {

		try {
			while (true) {
				// Hämtar först till en tmp-vektor för att slippa blinkandet 
				Vector<Object> ballsRawTmp = null;
				try {
					ballsRawTmp = remote.getBalls();
				} catch (RemoteException re) {
					System.out.println("Exception generated: " + re.getMessage());
				}

				// Sudda de tidigare bollarna
				Graphics g = frame.getGraphics();
				g.setColor(frame.getBackground());
				for (int i = 0; i < ballsRaw.size(); i += 3) {
					int x = (Integer) ballsRaw.elementAt(i);
					int y = (Integer) ballsRaw.elementAt(i + 1);
					int r = (Integer) ballsRaw.elementAt(i + 2);
					g.fillOval(x, y, r, r);
				}

				// Rita de nya bollarna
				ballsRaw = ballsRawTmp;
				for (int i = 0; i < ballsRaw.size(); i = i + 3) {
					int x = (Integer) ballsRaw.elementAt(i);
					int y = (Integer) ballsRaw.elementAt(i + 1);
					int r = (Integer) ballsRaw.elementAt(i + 2);

					int blue = r * 255 / rMax;
					if (blue > 255)
						blue = 255;
					if (blue < 0)
						blue = 0;
					g.setColor(new Color(0, 0, blue));
					g.fillOval(x, y, r, r);
				}
			}
		} catch (Exception e) {
		}
		try {
			Thread.sleep(40);
		} catch (Exception e) {
		}
	}
}
