package rococo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import rococo.data.UserEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
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
        String avatar,
        @JsonIgnore
        TestData testData
) {

        public UserJson(@Nonnull String username) {
                this(username, null);
        }

        public UserJson(@Nonnull String username, @Nullable TestData testData) {
                this(null, username, null, null, null,  testData);
        }

        public UserJson(UUID id, String username) {
                this(id, username, null, null, null, null);
        }

        public UserJson addTestData(@Nonnull TestData testData) {
                return new UserJson(id, username, firstname, lastname,  avatar,  testData);
        }

        public static UserJson fromUserEntity(UserEntity entity){
                return new UserJson(
                        entity.getId(),
                        entity.getUsername(),
                        entity.getFirstname(),
                        entity.getLastname(),
                        entity.getAvatar()!= null && entity.getAvatar().length > 0 ? new String(entity.getAvatar(), StandardCharsets.UTF_8) : null,
                        null
                );
        }
}