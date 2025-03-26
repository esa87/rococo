package rococo.api;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.*;

public interface AuthClient {

    @GET("oauth2/authorize")
    Call<Void> getAuthorizeCookies(
        @Query("response_type") String responseType,
        @Query("client_id") String clientId,
        @Query("scope") String scope,
        @Query(value = "redirect_uri", encoded = true) String redirectUri,
        @Query("code_challenge") String codeChallenge,
        @Query("code_challenge_method") String codeChallengeMethod
    );

    @POST("login")
    @FormUrlEncoded
    Call<Void> sendAuthorizeData(
            @Field("_csrf") String csrf,
            @Field("username") String username,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("oauth2/token")
    Call<JsonNode> genToken(
            @Field("code") String code,
            @Field(value = "redirect_uri", encoded = true) String redirectUri,
            @Field("code_verifier") String codeVerifier,
            @Field("grant_type") String grantType,
            @Field("client_id") String clientId
    );


}
