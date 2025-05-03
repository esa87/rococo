package rococo.jupiter.extension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import rococo.api.AllureApiClient;
import rococo.model.CreateProjectRequestJson;
import rococo.model.ResultAllureFileJson;
import rococo.utils.GenerateListResultAllureFiles;

import java.util.List;

public class AllureSendResultFromApi implements SuiteExtension {

    @Override
    public void afterSuite() throws JsonProcessingException {
        if ("docker".equals(System.getProperty("test.env", System.getenv("test.env")))) {
            final AllureApiClient allureApiClient = new AllureApiClient();
            String projectId = "rococo";
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
