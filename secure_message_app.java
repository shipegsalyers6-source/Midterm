import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class secure_message_app {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("     Secure Message Application");
        System.out.println("=================================");

        try {
            System.out.print("Enter a message: ");
            String message = scanner.nextLine();

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();

            String originalHash = generateSHA256(message);

            String encryptedMessage = encrypt(message, secretKey);

            String decryptedMessage = decrypt(encryptedMessage, secretKey);

            String decryptedHash = generateSHA256(decryptedMessage);

            System.out.println();
            System.out.println("========== RESULTS ==========");

            System.out.println("Original Message:");
            System.out.println(message);

            System.out.println();
            System.out.println("SHA-256 Hash of Original Message:");
            System.out.println(originalHash);

            System.out.println();
            System.out.println("AES Encrypted Message:");
            System.out.println(encryptedMessage);

            System.out.println();
            System.out.println("Decrypted Message:");
            System.out.println(decryptedMessage);

            System.out.println();
            System.out.println("SHA-256 Hash of Decrypted Message:");
            System.out.println(decryptedHash);

            System.out.println();
            System.out.println("========== INTEGRITY CHECK ==========");

            if (originalHash.equals(decryptedHash)) {
                System.out.println("Integrity Verified: Hashes match.");
                System.out.println("The decrypted message was not changed.");
            } else {
                System.out.println("Integrity Check Failed: Hashes do not match.");
                System.out.println("The message may have been changed.");
            }

            System.out.println();
            System.out.println("The AES key was randomly generated for this run.");

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        scanner.close();
    }

    public static String generateSHA256(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] hashBytes = digest.digest(
                input.getBytes(StandardCharsets.UTF_8)
        );

        StringBuilder hexString = new StringBuilder();

        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);

            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static String encrypt(String message, SecretKey key)
            throws Exception {

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(
                message.getBytes(StandardCharsets.UTF_8)
        );

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedMessage, SecretKey key)
            throws Exception {

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] encryptedBytes =
                Base64.getDecoder().decode(encryptedMessage);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}