package rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateProjectRequestJson(
        @JsonProperty("id")
        String projectId
) {}
