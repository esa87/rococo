package rococo.model.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import rococo.model.PaintingJson;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaintingPageResponse {
    private List<PaintingJson> content;
}
