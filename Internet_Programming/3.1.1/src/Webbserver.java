/*
 * Assignment 3.1.1 for IPROG course, about webb server connection 
 * @author Oliver Gallo olga7031
 */
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Webbserver {
	
	private String link;

	public static void main(String[] args) {
		Webbserver server = new Webbserver();
		
		server.link = "https://dsv.su.se/";
		
		if(args.length > 0) {
			server.link = args[0].toString(); 
		}
 
		server.webbpageLocator(server.link);
	}
	
	/*
	 * Locates the webb page and prints its content
	 * 
	 * @param text is the given webb page
	 */
	private void webbpageLocator(String text) {
		try {
			//Read in a new url webb page
			URL page = new URL(text);
			
			//Scanner is used to read in the content of the page, the openStream method opens up a connection and returns an inputStream
			Scanner scanner = new Scanner(page.openStream()).useDelimiter("\\A");
			
			//StringBuffer is used to hold the result, this is creates a thread safe and mutable string 
			StringBuffer buffer = new StringBuffer(); 
			
			while(scanner.hasNext()) {
				buffer.append(scanner.next());
			}
			
			String result = buffer.toString();
			System.out.println(result);
			
		} catch (IOException e) {
			e.printStackTrace(); 
		} 
	}
}
