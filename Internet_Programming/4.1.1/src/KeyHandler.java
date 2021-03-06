
/*
 * Assignment 4.1.1 for IPROG course, about encryption 
 * @author Oliver Gallo olga7031
 */
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/*
 * This class was influenced by the explanation and tutorial at https://www.baeldung.com/java-aes-encryption-decryption 
 * and uses AES encryption 
 */
public class KeyHandler {

	public static void main(String[] args) {
		SecretKey k = generateKey();
		convertKeyToString(k);
		saveKey(k, args[0]);
	}

	/*
	 * Generate and return the secret key
	 * 
	 * @return secret key
	 */
	private static SecretKey generateKey() {
		SecretKey secret = null;
		try {
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			generator.init(128);
			secret = generator.generateKey();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return secret;
	}

	/*
	 * Converts the key to a string
	 * 
	 * @param key is the secret key
	 * 
	 * @return converted key
	 */
	private static String convertKeyToString(SecretKey key) {

		byte[] buffer = key.getEncoded();
		String encodedKey = Base64.getEncoder().encodeToString(buffer);
		return encodedKey;
	}

	/*
	 * Save key to a file
	 * 
	 * @param key is the secret key
	 * 
	 * @param file is the file to save key in
	 */
	private static void saveKey(SecretKey key, String file) {
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(convertKeyToString(key));
			writer.close();
			System.out.println("Saved key");
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
