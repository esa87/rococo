package rococo.config;

public interface Config {

    static Config getInstance(){
        return LocalConfig.INSTANCE;
    }

    String frontUrl();

    String authUrl();

    String gatewayUrl();

    String userGrpcAddress();

    String artistGrpcAddress();

    String countryGrpcAddress();

    String museumGrpcAddress();

    String paintingGrpcAddress();

    default int userGrpcPort() {
        return 8089;
    }

    default int artistGrpcPort() {
        return 8091;
    }

    default int countryGrpcPort() {
        return 8092;
    }

    default int museumGrpcPort() {
        return 8093;
    }

    default int paintingGrpcPort() {
        return 8094;
    }

}
