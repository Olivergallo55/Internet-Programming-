import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JPanel;

class Paper extends JPanel {
	private HashSet<Point> hs = new HashSet<Point>();
	private Server server;

	public Paper(String[] args) {
		setBackground(Color.white);
		addMouseListener(new L1());
		addMouseMotionListener(new L2());
		server = new Server(this, args);
		Thread th = new Thread(server);
		th.start();
	}

	public void paintComponent(Graphics g) {
		try {
			super.paintComponent(g);
			g.setColor(Color.black);
			Iterator<Point> i = hs.iterator();
			while (i.hasNext()) {
				Point p = (Point) i.next();
				g.fillOval(p.x, p.y, 10, 10);
			}
		} catch (Exception e) {
		}
	}

	// adds the point on the screen and send it as a string
	private void addPoint(Point p) throws Exception {
		hs.add(p);
		repaint();
		String message = Integer.toString(p.x) + " " + Integer.toString(p.y);
		server.send(message);
	}

	// takes a string and converts it to a point in order to paint it on the screen
	public void addPoint(String string) {
		String[] xy = string.split(" ");
		Point p = new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
		hs.add(p); 
		repaint();
	}

	class L1 extends MouseAdapter {
		public void mousePressed(MouseEvent me) {
			try {
				addPoint(me.getPoint());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	class L2 extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent me) {
			try {
				addPoint(me.getPoint());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}