package rococo.utils;

import rococo.model.ResultAllureFileJson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class GenerateListResultAllureFiles {

    public static List<ResultAllureFileJson> processResultsFiles(String directoryPath) {
        List<ResultAllureFileJson> results = new ArrayList<>();
        Path resultsDirPath = Paths.get(directoryPath).toAbsolutePath().normalize();
        File resultsDir = resultsDirPath.toFile();
        if (resultsDir.exists() && resultsDir.isDirectory()) {
            for (File file : Objects.requireNonNull(resultsDir.listFiles())) {
                if (file.isFile()) {
                    try {
                        byte[] fileContent = Files.readAllBytes(file.toPath());
                        if (fileContent.length > 0) {
                            String base64Content = Base64.getEncoder().encodeToString(fileContent);
                            results.add(new ResultAllureFileJson(file.getName(), base64Content));
                        } else {
                            System.out.println("Пустой файл пропущен: " + file.getAbsolutePath());
                        }
                    } catch (IOException e) {
                        System.err.println("Ошибка чтения файла: " + file.getAbsolutePath());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Директория пропущена: " + file.getAbsolutePath());
                }
            }
        }
        return results;
    }
}
