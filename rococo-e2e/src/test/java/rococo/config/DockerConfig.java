package rococo.config;

public enum DockerConfig implements Config{
    INSTANCE;

    @Override
    public String frontUrl() {
        return "http://frontend.rococo.dc/";
    }

    @Override
    public String authUrl() {
        return "http://auth.rococo.dc:9000/";
    }

    @Override
    public String gatewayUrl() {
        return "http://gateway.rococo.dc:8080/";
    }

    @Override
    public String allureUrl() {
        return "http://allure:5050";
    }

    @Override
    public String userGrpcAddress() {
        return "userdata.rococo.dc";
    }

    @Override
    public String artistGrpcAddress() {
        return "artist.rococo.dc";
    }

    @Override
    public String countryGrpcAddress() {
        return "country.rococo.dc";
    }

    @Override
    public String museumGrpcAddress() {
        return "museum.rococo.dc";
    }

    @Override
    public String paintingGrpcAddress() {
        return "painting.rococo.dc";
    }
}
