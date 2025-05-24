package rococo.jupiter.extension;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.extension.ExtensionContext;
import rococo.api.AllureApiClient;
import rococo.model.CreateProjectRequestJson;
import rococo.model.ResultAllureFileJson;
import rococo.utils.GenerateListResultAllureFiles;

import java.util.List;

public class AllureSendResultFromApi implements SuiteExtension {

    private static final boolean inDocker = "docker".equals(System.getProperty("test.env", System.getenv("test.env")));
    private static final AllureApiClient allureApiClient = new AllureApiClient();
    private static final String projectId = "rococo-esa87";

    @Override
    public void beforeSuite(ExtensionContext context) {
        if (inDocker) {
            int checkCreateProject = allureApiClient.requestGetProjectsById(projectId);
            if (checkCreateProject == 200) {
                allureApiClient.requestCleanResults(projectId);
            }
        }
    }

    @Override
    public void afterSuite() throws JsonProcessingException {
        if (inDocker) {
            int needCreateProject = allureApiClient.requestGetProjectsById(projectId);
            if (needCreateProject != 200) {
                CreateProjectRequestJson projectRequestJson = new CreateProjectRequestJson(projectId);
                allureApiClient.requestCreateProjects(projectRequestJson);
            }
            List<ResultAllureFileJson> resultAllureFileJsonList = GenerateListResultAllureFiles.processResultsFiles("rococo-e2e/build/allure-results");
            allureApiClient.requestSendResult(projectId, resultAllureFileJsonList);
            allureApiClient.requestGenerateReport(projectId);
        }
    }
}
