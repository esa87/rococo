package rococo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nonnull;
import java.util.List;

public record TestData(
        @JsonIgnore @Nonnull String password
      ) {

    public TestData(@Nonnull String password) {
        this.password = password;
    }

    private @Nonnull String[] extractUsernames(List<UserJson> users) {
        return users.stream().map(UserJson::username).toArray(String[]::new);
    }
}