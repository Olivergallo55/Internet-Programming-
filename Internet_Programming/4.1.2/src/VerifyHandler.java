/*
 * Assignment 4.1.2 for IPROG course, about Signing/Verification 
 * @author Oliver Gallo olga7031
 */
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public class VerifyHandler {

	public static void main(String[] args) {
		PublicKey key = getPublicKey(args[1]);
		initVerification(args[0], key, args[2]);

	}

	/*
	 * Initiates the verification process, retains the public key, signature and
	 * connects it to the data file
	 * 
	 * @param datafile
	 * 
	 * @param publicKey
	 * 
	 * @param signatureFile
	 */
	private static void initVerification(String datafile, PublicKey publicKey, String signatureFile) {
		Signature signature = null;

		try {
			FileInputStream input = new FileInputStream(signatureFile);
			byte[] signatureHolder = new byte[input.available()];
			input.read(signatureHolder);
			signature = Signature.getInstance("SHA256withDSA", "SUN");
			signature.initVerify(publicKey);

			connectDataWithSignature(signature, datafile);

			boolean verify = signature.verify(signatureHolder);

			if (verify)
				System.out.println("signature is verifyed");
			else
				System.err.println("signature is not acceptet");
			input.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Supplies the data with the signature that is obtained from the .txt file
	 * 
	 * @param signature is the signature
	 * 
	 * @param dataFile is the data file
	 */
	private static void connectDataWithSignature(Signature signature, String dataFile) {
		try {
			FileInputStream input = new FileInputStream(dataFile);
			BufferedInputStream stream = new BufferedInputStream(input);
			byte[] buffer = new byte[1024];
			int length;
			while (stream.available() != 0) {
				length = stream.read(buffer);
				signature.update(buffer, 0, length);
			}
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Gets the public key
	 * 
	 * @param data is the .txt file for the public key
	 * 
	 * @return the public key
	 */
	private static PublicKey getPublicKey(String data) {
		PublicKey key = null;
		try {
			FileInputStream input = new FileInputStream(data);
			byte[] keyHolder = new byte[input.available()];
			input.read(keyHolder);
			input.close();

			X509EncodedKeySpec publicKey = new X509EncodedKeySpec(keyHolder);
			KeyFactory factory = KeyFactory.getInstance("DSA", "SUN");
			key = factory.generatePublic(publicKey);
			input.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return key;
	}
}
