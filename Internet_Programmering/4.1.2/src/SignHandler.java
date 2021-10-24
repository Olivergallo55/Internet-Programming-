import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignHandler {

	public static void main(String[] args)
			throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
		PrivateKey key = getPrivateKey("C:/Users/Admin/Desktop/Internet_Programmering/4.1.2/bin/SecretKey.txt");
		signData(key, "C:/Users/Admin/Desktop/Internet_Programmering/4.1.2/bin/data.txt", 
				"C:/Users/Admin/Desktop/Internet_Programmering/4.1.2/bin/signature.txt"); 
		
			System.out.println("The file have been signed");
	}

	//This method takes a signature and initializes it with the private key and returns the signature
	private static Signature initSigngData(PrivateKey key, String dataFile) {
		Signature signature = null;
		try {
			signature = Signature.getInstance("SHA256withDSA", "SUN");
			signature.initSign(key);
			FileInputStream inputStream = new FileInputStream(dataFile);
			BufferedInputStream stream = new BufferedInputStream(inputStream);
			byte[] buffer = new byte[1024];
			int length;

			while ((length = stream.read(buffer)) >= 0) {
				signature.update(buffer, 0, length);
			}
			stream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return signature;
	}

	//Method to sign the data with the generated private key and then save to a new signature file
	private static void signData(PrivateKey key, String dataFile, String outputFile) {
		try {
			Signature signature = initSigngData(key, dataFile);
			byte[] sign = signature.sign();
			FileOutputStream output = new FileOutputStream(outputFile);
			output.write(sign);
			output.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//This method loads the Private key from the raw .txt file and returns it
	private static PrivateKey getPrivateKey(String file) {
		PrivateKey privateKey = null;
		try {
			FileInputStream input = new FileInputStream(file);
			byte[] inputData = new byte[input.available()];
			input.read(inputData);

			PKCS8EncodedKeySpec pubKeySpec = new PKCS8EncodedKeySpec(inputData);
			KeyFactory factory = KeyFactory.getInstance("DSA", "SUN");
			privateKey = factory.generatePrivate(pubKeySpec);
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return privateKey;
	}
}
