
/*
 * Assignment 2.1.3 for IPROG course, about stream sockets and XML 
 * @author Oliver Gallo olga7031
 */
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

	private Client client;

	public XMLSender(Client client) {
		this.client = client;
	}

	/*
	 * This method comes from
	 * https://stackoverflow.com/questions/21001305/build-an-xml-with-jdom2-and-add-
	 * data with modification This document is a Tree with the elements as child
	 * nodes, the message element is the root node
	 */
	public Document createXML(String name, String email, String homepage, String msg) {
		// make the doctype together with the dtd file
		DocType docType = new DocType("message", "1//PW//Example//123",
				"https://people.dsv.su.se/~pierre/i/05_ass/ip1/2/2.1.3/message.dtd");

		// root element
		Element messageElement = new Element("message");

		// header element
		Element headerElement = new Element("header");
		messageElement.addContent(headerElement);

		// supercars element
		Element protocolElement = new Element("protocol");
		protocolElement.addContent(new Element("type").setText("CTTP"));
		protocolElement.addContent(new Element("version").setText("1.0"));
		protocolElement.addContent(new Element("command").setText("MESS"));

		Element idElement = new Element("id");
		idElement.addContent(new Element("name").setText(name));
		idElement.addContent(new Element("email").setText(email));
		idElement.addContent(new Element("homepage").setText(homepage));
		idElement.addContent(new Element("host").setText("unknown"));

		Element bodyElement = new Element("body");
		messageElement.addContent(bodyElement);
		headerElement.addContent(protocolElement);
		headerElement.addContent(idElement);
		bodyElement.addContent(msg);

		return new Document(messageElement, docType);
	}


	/*
	 * Prints the XML
	 * 
	 * @param decides if its input or output --> its for debug
	 * @param document is the XML file
	 */
	private void printXML(String result, Document document) {
		try {
			XMLOutputter xmlOutput = new XMLOutputter();
			System.out.println(result);
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(document, System.out);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Reads the name and email
	 * 
	 * @param read is the element to be read
	 */
	private static void readName(Element read) {
		GUI.area.append('<' + read.getChild("id").getChildText("name") + '>' + " " + "(<"
				+ read.getChild("id").getChildText("email") + ">)" + " ");
	}

	/*
	 * Reads the message 
	 * 
	 * @param read is the element to be read
	 */
	private static void readMessage(Element read) {
		GUI.area.append('<' + read.getText() + '>' + "\n");
	}

	// Inspiration taken from
	// https://howtodoinjava.com/java/xml/jdom2-read-parse-xml-examples/ but
	// implemented with modification
	public void reciveXML(String document) {
		Document doc = null;
		SAXBuilder b = new SAXBuilder();
		try {
			doc = b.build(new StringReader(document));
			printXML("\n###########################_INPUT_###########################", doc);
			Element root = doc.getRootElement();
			root.getChildren("header").forEach(XMLSender::readName);
			root.getChildren("body").forEach(XMLSender::readMessage);

		} catch (JDOMException | IOException e) {
			System.err.println("Kunnde tyvärr inte parsa");
		}
	}

	/*
	 * Sends the XML
	 * 
	 * @param document is the XML to be sent
	 */
	public void sendXML(Document document) {
		printXML("\n###########################_OUTPUT_###########################", document);
		Format format = Format.getCompactFormat(); // method pretty prints the xml file
		format.setLineSeparator("");
		XMLOutputter xmlOutputter = new XMLOutputter(format);
		send(xmlOutputter.outputString(document));
	}

	/*
	 * Sends the message
	 * 
	 * @param s is the messsage to be sent
	 */
	private void send(String s) {
		client.sendMessage(s);
	}
}
