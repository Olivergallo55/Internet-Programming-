/**
 * @author Oliver Gallo, Gesällprov
 **/
import java.io.IOException;
import java.io.StringReader;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter; 

public class XMLSender {

	public XMLSender() {
	}

	//Class creates xml files
	public XMLSender(String user, String receiver, String msg) {
		sendXML(createXML(user, receiver, msg));
	}

	/*
	 * Creates an xml documents with the parameters
	 * 
	 * @param user is the message sender
	 * @param receiver is the message receiver
	 * @param msg is the information the user want to send
	 */
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

	/*
	 * Prints out the XML message
	 * 
	 * @param document is the xml file
	 */
	private void printXML(Document document) {
		try {
			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(document, System.out);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Receive the message and build it as an xml
	 * 
	 * @param document is the received message
	 */
	public void reciveXML(String document) {
		Document doc = null;
		SAXBuilder b = new SAXBuilder();
		try {
			doc = b.build(new StringReader(document));
			printXML(doc);
			Element root = doc.getRootElement();

			root.getChildren("header").forEach(XMLSender::readName);
			root.getChildren("body").forEach(XMLSender::readMessage);

		} catch (JDOMException | IOException e) {
			e.printStackTrace();
			System.err.println("Kunnde tyvärr inte parsa");
		}
	}

	/*
	 * Append the name
	 * 
	 * @param read is the name
	 */
	private static void readName(Element read) {
		ChatScene.area.append(read.getChild("id").getChildText("username") + ": ");
	}
	/*
	 * Append the sent message
	 * 
	 * @param read is the message
	 */
	private static void readMessage(Element read) {
		ChatScene.area.append(read.getText() + "\n");
	}

	/*
	 * Send xml as a string
	 * 
	 * @param takes the xml
	 */
	public void sendXML(Document document) {
		printXML(document);
		Format format = Format.getCompactFormat(); // method pretty prints the xml file
		format.setLineSeparator("");
		XMLOutputter xmlOutputter = new XMLOutputter(format);
		User.sendMessage(xmlOutputter.outputString(document));
	}

}
