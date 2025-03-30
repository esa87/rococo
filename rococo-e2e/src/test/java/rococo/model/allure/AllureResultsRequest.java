package rococo.model.allure;

import rococo.model.ResultAllureFileJson;

import java.util.List;

public class AllureResultsRequest {
    private final List<ResultAllureFileJson> results;

    public AllureResultsRequest(List<ResultAllureFileJson> results) {
        this.results = results;
    }

    public List<ResultAllureFileJson> getResults() {
        return results;
    }
}
