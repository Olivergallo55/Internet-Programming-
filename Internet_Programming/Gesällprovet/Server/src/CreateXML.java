/**
 * @author Oliver Gallo, Gesällprov
 **/
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class CreateXML {

	private Server server;

	public CreateXML(Server server) {
		this.server = server;
	}

	public CreateXML(String user, String receiver, String msg, Server server) {
		this.server = server;
		createXML(user, receiver, msg);
	}

	// Create XML to send when a client connects/disconnects
	public Document createXML(String user, String receiver, String msg) {
		DocType docType = new DocType("message");
		// Root node
		Element messageElement = new Element("message");

		// header element
		Element headerElement = new Element("header");
		messageElement.addContent(headerElement);

		// protocol element
		Element protocolElement = new Element("protocol");
		protocolElement.addContent(new Element("type").setText("CTTP"));
		protocolElement.addContent(new Element("version").setText("1.0"));
		protocolElement.addContent(new Element("command").setText("MESS"));

		// id elements
		Element idElement = new Element("id");
		idElement.addContent(new Element("username").setText(user));
		idElement.addContent(new Element("receiver").setText(receiver));

		// body elements
		Element bodyElement = new Element("body");
		messageElement.addContent(bodyElement);
		headerElement.addContent(protocolElement);
		headerElement.addContent(idElement);
		bodyElement.addContent(msg);

		return new Document(messageElement, docType);
	}

	// Send XML files
	public void sendXML(Document document) {
		//printXML("i send this \n", document);
		Format format = Format.getCompactFormat(); // method pretty prints the xml file
		format.setLineSeparator("");
		XMLOutputter xmlOutputter = new XMLOutputter(format);
		server.broadcast(xmlOutputter.outputString(document)); 
	}

	//Receive XML files
	public void reciveXML(String msg) {
		Document doc = null;
		SAXBuilder b = new SAXBuilder();
		try {
			doc = b.build(new StringReader(msg));
			Element root = doc.getRootElement();
			root.getChildren("header").forEach(CreateXML::readName);
			root.getChildren("body").forEach(CreateXML::readMessage);
			printReceiver(msg);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
			System.err.println("Kunnde tyvärr inte parsa");
		}
	}

	// Get the receiver from the received string
	public void printReceiver(String user) {
		Document doc = null;
		SAXBuilder b = new SAXBuilder();
		try {
			doc = b.build(new StringReader(user));
			Element root = doc.getRootElement();
			root.getChildren("header").forEach(CreateXML::getReceiver);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
			System.err.println("Kunnde inte hämta mottagaren");
		}
	}

	// Gets and returns the sender
	public String getSender(String user) {
		Document doc = null;
		SAXBuilder b = new SAXBuilder();
		String sender = null;
		try {
			doc = b.build(new StringReader(user));
			Element root = doc.getRootElement();
			Element message = root.getChild("header");
			List<Element> elements = message.getChildren("id");

			for (Element e : elements)
				sender = e.getChildText("username");

		} catch (JDOMException | IOException e) {
			e.printStackTrace();
			System.err.println("Kunnde inte hämta mottagaren");
		}
		return sender;
	}

	// Gets and returns the receiver
	public String getReceiver(String user) {
		Document doc = null;
		SAXBuilder b = new SAXBuilder();
		String sender = null;
		try {
			doc = b.build(new StringReader(user));
			Element root = doc.getRootElement();
			Element message = root.getChild("header");
			List<Element> elements = message.getChildren("id");

			for (Element e : elements)
				sender = e.getChildText("receiver");

		} catch (JDOMException | IOException e) {
			e.printStackTrace();
			System.err.println("Kunnde inte hämta mottagaren");
		}
		return sender;
	}

	//Get the message
	public String getMessage(String user) {
		Document doc = null;
		SAXBuilder b = new SAXBuilder();
		String sender = null;
		try {
			doc = b.build(new StringReader(user));
			Element root = doc.getRootElement();
			sender = root.getChildText("body");

		} catch (JDOMException | IOException e) {
			e.printStackTrace();
			System.err.println("Kunnde inte hämta mottagaren");
		}
		return sender;
	}

	// Gets the receiver
	private static String getReceiver(Element receiver) {
		return receiver.getChild("id").getChildText("receiver");
	}

	// Print out the name
	private static void readName(Element read) {
		System.out.println(read.getChild("id").getChildText("username") + " send to: "
				+ read.getChild("id").getChildText("receiver") + " with the message: ");
	}

	// Print out the message
	private static void readMessage(Element read) {
		System.out.println(read.getText() + "\n");
	}

//	private void printXML(String text, Document document) {
//		try {
//			XMLOutputter xmlOutput = new XMLOutputter();
//			System.out.println(text);
//			xmlOutput.setFormat(Format.getPrettyFormat());
//			xmlOutput.output(document, System.out);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
