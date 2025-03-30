package rococo.api;

import retrofit2.Call;
import retrofit2.http.*;
import rococo.model.CreateProjectRequestJson;
import rococo.model.allure.AllureResultsRequest;

import javax.annotation.Nullable;

public interface AllureApi {

    @POST("/allure-docker-service/projects")
    Call<Void> createProjects(
            @Body CreateProjectRequestJson createProjectRequest
    );

    @POST("/allure-docker-service/send-results")
    Call<Void> sendResult(
            @Query("project_id") @Nullable String projectId,
            @Query("force_project_creation") @Nullable Boolean forceProjectCreation,
            @Body AllureResultsRequest request
    );

    @GET("/allure-docker-service/generate-report")
    Call<Void> generateReport(
            @Query("project_id") String projectId,
            @Query("execution_name") @Nullable String executionName,
            @Query("execution_from") @Nullable String executionFrom,
            @Query("execution_type") @Nullable String executionType,
            @Query("clean") @Nullable Boolean cleanReport
    );

    @GET("/allure-docker-service/projects/{id}")
    Call<Void> getProjectsById(
           @Path("id") String id
    );
}
