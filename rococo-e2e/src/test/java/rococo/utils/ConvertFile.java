package rococo.utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import static java.net.URLEncoder.encode;

public class ConvertFile {

    public static String encode(String filePath) throws Exception {
        byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public static String encodeImageWithMime(String filePath) throws Exception {
        String mimeType = Files.probeContentType(Paths.get(filePath));
        if (mimeType == null) {
            // Если тип не определился, используем image/jpeg как fallback
            mimeType = "image/jpeg";
        }

        // Кодируем файл в Base64
        String base64 = encode(filePath);

        // Формируем Data URL
        return "data:" + mimeType + ";base64," + base64;
    }
}
