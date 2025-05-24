package rococo.api;

import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import rococo.config.Config;
import rococo.model.CreateProjectRequestJson;
import rococo.model.ResultAllureFileJson;
import rococo.model.allure.AllureResultsRequest;
import rococo.service.RestClient;

import java.io.IOException;
import java.util.List;

public class AllureApiClient extends RestClient {

    private final static Config CFG = Config.getInstance();
    private final AllureApi allureApi;

    public AllureApiClient() {
        super(CFG.allureUrl(), HttpLoggingInterceptor.Level.NONE);
        this.allureApi = retrofit.create(AllureApi.class);
    }

    public void requestCreateProjects(CreateProjectRequestJson createProjectRequestJson) {
        final Response<Void> response;
        try {
            response = allureApi.createProjects(createProjectRequestJson).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertTrue(201 == response.code());
    }

    public void requestGenerateReport(String projectId) {
        final Response<Void> response;
        try {
            response = allureApi.generateReport(
                    projectId,
                    System.getenv("HEAD_COMMIT_MESSAGE"),
                    System.getenv("BUILD_URL"),
                    System.getenv("EXECUTION_TYPE")
            ).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertTrue(200 == response.code());
    }


    public void requestSendResult(String projectId, List<ResultAllureFileJson> resultAllureFileJsons) {
        final Response<Void> response;
        AllureResultsRequest request = new AllureResultsRequest(resultAllureFileJsons);
        try {
            response = allureApi.sendResult(projectId, false, request).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertTrue(200 == response.code());
    }


    public int requestGetProjectsById(String id) {
        final Response<Void> response;
        try {
            response = allureApi.getProjectsById(id).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return response.code();
    }


    public void requestCleanResults(String projectId){
        final Response<Void> response;
        try {
            response = allureApi.cleanResults(projectId).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertTrue(200 == response.code());
    }

}
