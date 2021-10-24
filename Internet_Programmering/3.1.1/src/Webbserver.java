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
	
	private void webbpageLocator(String text) {
		try {
			//Read in a new url webbpage
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
