/*
 * Assignment 4.1.2 for IPROG course, about Signing/Verification 
 * @author Oliver Gallo olga7031
 */
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;


//this code followed the following link with some own modification
// https://docs.oracle.com/javase/tutorial/security/apisign/vstep2.html

public class KeyHandler {

	public static void main(String[] args) {

		KeyPair keys = generateKeyPair();
		saveKeysToFile(keys, args[1], args[0]);  
	}

	/*
	 * Generates key pairs
	 */
	private static KeyPair generateKeyPair() {
		KeyPair keys = null; 
		try {
			// Create and initilize the keyPairs
			KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("DSA", "SUN");
			keyGenerator.initialize(2048);

			// generate keyPair
			keys = keyGenerator.generateKeyPair();

		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		} 
		return keys;
	}

	/*
	 * Saves key to the file
	 * 
	 * @param keys is the keys
	 * 
	 * @param publicKey is the file for the public key
	 * 
	 * @param secretKey is the file for the private key
	 */
	private static void saveKeysToFile(KeyPair keys, String publicKey, String secretKey) {
		try {
			FileOutputStream outputPublicKey = new FileOutputStream(publicKey);
			FileOutputStream outputPrivateKey = new FileOutputStream(secretKey);

			outputPublicKey.write(keys.getPublic().getEncoded());
			outputPublicKey.close();

			outputPrivateKey.write(keys.getPrivate().getEncoded());
			outputPrivateKey.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
