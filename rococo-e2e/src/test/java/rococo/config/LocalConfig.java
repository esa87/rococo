package rococo.config;

public enum LocalConfig implements Config{
    INSTANCE;

    @Override
    public String frontUrl() {
        return "http://127.0.0.1:3000/";
    }

    @Override
    public String authUrl() {
        return "http://127.0.0.1:9000/";
    }

    @Override
    public String gatewayUrl() {
        return "http://127.0.0.1:8080/";
    }

    @Override
    public String userGrpcAddress() {
        return "127.0.0.1";
    }

    @Override
    public String artistGrpcAddress() {
        return "127.0.0.1";
    }

    @Override
    public String countryGrpcAddress() {
        return "127.0.0.1";
    }

    @Override
    public String museumGrpcAddress() {
        return "127.0.0.1";
    }

    @Override
    public String paintingGrpcAddress() {
        return "127.0.0.1";
    }
}
