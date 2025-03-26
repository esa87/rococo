package rococo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "grpc.client")
public class GrpcClientProperties {
    private ClientConfig rococoArtist;
    private ClientConfig rococoCountry;
    private ClientConfig rococoMuseum;
    private ClientConfig rococoPainting;
    private ClientConfig rococoUserdata;

    @Setter
    @Getter
    public static class ClientConfig {
        private String address;
        private String negotiationType;

    }
}
