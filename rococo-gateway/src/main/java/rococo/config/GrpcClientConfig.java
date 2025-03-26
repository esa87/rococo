package rococo.config;


import grpc.rococo.*;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


@Configuration
public class GrpcClientConfig {
    private final GrpcClientProperties grpcClientProperties;

    public GrpcClientConfig(GrpcClientProperties grpcClientProperties) {
        this.grpcClientProperties = grpcClientProperties;
    }

    @Bean
    public ManagedChannel artistServiceChannel() {
        return createChannel(grpcClientProperties.getRococoArtist());
    }

    @Bean
    public ManagedChannel countryServiceChannel() {
        return createChannel(grpcClientProperties.getRococoCountry());
    }

    @Bean
    public ManagedChannel museumServiceChannel() {
        return createChannel(grpcClientProperties.getRococoMuseum());
    }

    @Bean
    public ManagedChannel paintingServiceChannel() {
        return createChannel(grpcClientProperties.getRococoPainting());
    }

    @Bean
    public ManagedChannel userdataServiceChannel() {
        return createChannel(grpcClientProperties.getRococoUserdata());
    }

    @Bean
    public ArtistServiceGrpc.ArtistServiceFutureStub artistServiceFutureStub(ManagedChannel artistServiceChannel) {
        return ArtistServiceGrpc.newFutureStub(artistServiceChannel);
    }

    @Bean
    public CountryServiceGrpc.CountryServiceFutureStub countryServiceFutureStub(ManagedChannel countryServiceChannel) {
        return CountryServiceGrpc.newFutureStub(countryServiceChannel);
    }

    @Bean
    public MuseumServiceGrpc.MuseumServiceFutureStub museumServiceFutureStub(ManagedChannel museumServiceChannel) {
        return MuseumServiceGrpc.newFutureStub(museumServiceChannel);
    }

    @Bean
    public PaintingServiceGrpc.PaintingServiceFutureStub paintingServiceFutureStub(ManagedChannel paintingServiceChannel) {
        return PaintingServiceGrpc.newFutureStub(paintingServiceChannel);
    }

    @Bean
    public UserdataServiceGrpc.UserdataServiceFutureStub userdataServiceFutureStub(ManagedChannel userdataServiceChannel) {
        return UserdataServiceGrpc.newFutureStub(userdataServiceChannel);
    }

    private static final Logger log = LoggerFactory.getLogger(GrpcClientConfig.class);

    public ManagedChannel createChannel(GrpcClientProperties.ClientConfig config) {
        try {
            String[] addressParts = config.getAddress().replace("static://", "").split(":");
            String host = addressParts[0];
            int port = Integer.parseInt(addressParts[1]);

            return ManagedChannelBuilder.forAddress(host, port)
                    .keepAliveTime(120, TimeUnit.SECONDS)  // Увеличьте интервал
                    .keepAliveTimeout(10, TimeUnit.SECONDS)
                    .keepAliveWithoutCalls(true)  // Отключите, если не нужно
                    .maxRetryAttempts(3)  // Ограничьте попытки реконнекта
                    .idleTimeout(5, TimeUnit.MINUTES)
                    .usePlaintext() // В production заменить на TLS
                    .build();
        } catch (Exception e) {
            log.error("Failed to create gRPC channel for {}", config.getAddress(), e);
            throw new IllegalStateException("gRPC channel initialization failed", e);
        }
    }


}
