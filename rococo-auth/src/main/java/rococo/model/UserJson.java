package rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserJson(
    @JsonProperty("username")
    String username) {

}
