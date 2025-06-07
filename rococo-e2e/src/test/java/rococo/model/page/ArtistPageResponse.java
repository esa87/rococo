package rococo.model.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import rococo.model.ArtistJson;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtistPageResponse {
    private List<ArtistJson> content;
}
