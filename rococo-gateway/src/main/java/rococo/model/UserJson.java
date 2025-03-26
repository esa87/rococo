package rococo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import grpc.rococo.UserResponse;

import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("firstname")
        String firstname,
        @JsonProperty("lastname")
        String lastname,
        @JsonProperty("avatar")
        String avatar
) {

        public static UserJson fromUserResponse(UserResponse response){
                return new UserJson(
                        UUID.fromString(response.getId()),
                        response.getUsername(),
                        response.getFirstname(),
                        response.getLastname(),
                        response.getAvatar()
                );
        }
}