package rococo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import rococo.data.CountryEntity;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResultAllureFileJson(
        @JsonProperty("file_name")
        String fileName,
        @JsonProperty("content_base64")
        String contentBase64
) {
}