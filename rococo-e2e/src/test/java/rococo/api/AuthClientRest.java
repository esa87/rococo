package rococo.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import rococo.api.interceptors.CodeInterceptor;
import rococo.config.Config;
import rococo.jupiter.extension.ApiLoginExtension;
import rococo.service.RestClient;
import rococo.service.ThreadSafeCookieStore;
import rococo.utils.OauthUtils;

import java.io.IOException;

public class AuthClientRest extends RestClient {

    protected final static Config CFG = Config.getInstance();
    private final AuthClient authClient;
    private static String responseType = "code";
    private static String clientId = "client";
    private static String scope = "openid";
    private static String redirectUri = CFG.frontUrl() + "authorized";
    private static String codeChallengeMethod = "S256";
    private static String grantType = "authorization_code";

    public AuthClientRest() {
        super(CFG.authUrl(), true, new CodeInterceptor());
        this.authClient = retrofit.create(AuthClient.class);
    }


    public String getToken(String username, String password){
        final String code_verifier = OauthUtils.generateCodeVerifier();
        final String code_challenge = OauthUtils.generateCodeChallange(code_verifier);
        getAuthorizeCookies(code_challenge);
        sendAuthorizeData(username, password);
        return genToken(code_verifier).toString();
    }

    private void getAuthorizeCookies(String code_challenge) {

        Response<Void> response;
        try {
            response = authClient.getAuthorizeCookies(responseType, clientId, scope, redirectUri, code_challenge, codeChallengeMethod)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(200, response.code());
    }

    private String sendAuthorizeData(String username, String password) {
        Response<Void> response;
        try {
            response = authClient.sendAuthorizeData(
                            ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN"),
                            username,
                            password
                    )
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(200, response.code());
        String urlFromRedirect = response.raw().request().url().toString();
        return urlFromRedirect.substring(urlFromRedirect.lastIndexOf("=") + 1);
    }

    @Step("Code verifier {code_verifier}")
    private String genToken(String code_verifier) {
        Response<JsonNode> response;
        try {
            response = authClient.genToken(
                            ApiLoginExtension.getCode(),
                            redirectUri,
                            code_verifier,
                            grantType,
                            clientId
                    )
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(200, response.code());
        return response.body().get("id_token").asText();
    }
}
