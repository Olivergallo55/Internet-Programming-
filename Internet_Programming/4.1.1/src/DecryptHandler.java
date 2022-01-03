/*
 * Assignment 4.1.1 for IPROG course, about encryption 
 * @author Oliver Gallo olga7031
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class DecryptHandler {

	public static void main(String[] args) {
		
		SecretKey key = readKey(args[1]);
		decryptText(key, args[0], args[2]); 
	}
	
	/*
	 * Decrypts the input text with AES encryption
	 * 
	 * @param key is the secret key
	 * 
	 * @param encryptFile is the given encrypted text
	 * 
	 * @param decryptFile is the decrypted text
	 */
	private static void decryptText(SecretKey key, String encryptFile, String decryptFile) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			FileInputStream inputStream = new FileInputStream(encryptFile);
			FileOutputStream outputStream = new FileOutputStream(decryptFile);
			byte[] encryptedText = new byte[64];
			int textLength = 0;

			while((textLength = inputStream.read(encryptedText)) != -1) {
				byte[] textContent = cipher.update(encryptedText, 0, textLength);
				
				if(textContent != null) {
					outputStream.write(textContent);
				}
				byte[] resultData = cipher.doFinal();
				if(resultData != null) {
					outputStream.write(resultData);
				}
			}
			inputStream.close();
			outputStream.close(); 
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}	
	}
	
	
	/*
	 * Reads the key from a text
	 * 
	 * @param text is the secret key
	 * 
	 * @return the secret key
	 */
	private static SecretKey readKey(String text) {
		SecretKey secretKey = null;
		String result = null;
		Scanner scanner = null;
		File file = new File(text);
		try {
			scanner = new Scanner(file);

			while (scanner.hasNext()) {
				result = scanner.nextLine();
				byte[] decodedResult = Base64.getDecoder().decode(result);
				secretKey = new SecretKeySpec(decodedResult, 0, decodedResult.length, "AES");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		return secretKey;
	}
}
