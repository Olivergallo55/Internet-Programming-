import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptHandler {

	public static void main(String[] args) {
		SecretKey key = readKey(args[1]);
		encryptText(key, args[0], args[2]);
	}

	//used https://www.baeldung.com/java-aes-encryption-decryption for insperatrion
	private static void encryptText(SecretKey key, String inputFile, String outputFile) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			FileInputStream input = new FileInputStream(inputFile);
			FileOutputStream output = new FileOutputStream(outputFile);
			byte[] buffer = new byte[64];
			int readbytes;
			
			while((readbytes = input.read(buffer)) != -1) {
				byte[] outputbytes = cipher.update(buffer, 0, readbytes);
				if(outputbytes != null) {
					output.write(outputbytes);
				}
			}
			byte[] result = cipher.doFinal();
			if(result != null) {
				output.write(result);
			}
			input.close();
			output.close();
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
	}

	private static SecretKey readKey(String text) {
		SecretKey secretKey = null;
		String result = null;
		Scanner scanner = null;
		File file = new File(text);
		try {
			scanner = new Scanner(file);

			while (scanner.hasNext()) {
				result = scanner.nextLine();
				System.out.println("result: " + result);
				byte[] decodedResult = Base64.getDecoder().decode(result);
				secretKey = new SecretKeySpec(decodedResult, 0, decodedResult.length, "AES");
				System.out.println("decoded key: " + secretKey);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		return secretKey;
	}
}
