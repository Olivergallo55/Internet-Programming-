/*
 * Assignment 2.2.1 for IPROG course, about datagram socket with unicast 
 * @author Oliver Gallo olga7031
 * Insperation comes from https://www.baeldung.com/udp-in-java tutorial for UDP connections in java 
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server implements Runnable {

	@SuppressWarnings("unused")
	private int port;
	private int recive_port;
	private String host;
	@SuppressWarnings("unused")
	private Paper paper;
	private byte[] buf = new byte[256];
	private DatagramSocket socket;

	public Server(Paper paper, String[] host) {
		this.paper = paper;
		this.port = Integer.parseInt(host[0]);
		this.host = host[1];
		this.recive_port = Integer.parseInt(host[2]); 
	}

	/*
	 * Runs application
	 */
	@Override
	public void run() {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(port); //socket that is used to send and receive packets
			while (true) {
				DatagramPacket p = new DatagramPacket(buf, buf.length); 
				socket.receive(p);
				String s = new String(p.getData(), 0, p.getLength()); // string to be decoded
				paper.addPoint(s); //add the point to the canvas
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			socket.close();
		}
	}

	/*
	 * Sends message
	 * 
	 * @param msg is the message to be sent
	 */
	public void send(String msg) throws Exception {
		try {
			buf = msg.getBytes();
			socket = new DatagramSocket();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(host), recive_port);
			socket.send(packet);
		} finally {
			socket.close();
		}
	}
}
