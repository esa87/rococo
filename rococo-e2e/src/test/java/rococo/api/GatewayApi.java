package rococo.api;

import retrofit2.Call;
import retrofit2.http.*;
import rococo.model.ArtistJson;
import rococo.model.MuseumJson;
import rococo.model.PaintingJson;
import rococo.model.page.ArtistPageResponse;
import rococo.model.page.CountryPageResponse;
import rococo.model.page.MuseumPageResponse;
import rococo.model.page.PaintingPageResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface GatewayApi {

    // Методы для работы с картинами

    @GET("api/painting")
    Call<PaintingPageResponse> getAllPaintings(
            @Query("title") @Nullable String title,
            @Query("page") int page,
            @Query("size") int size,
            @Query("sort") @Nullable String sort,
            @Header("Authorization") @Nullable String bearerToken);

    @GET("api/painting/author/{id}")
    Call<PaintingPageResponse> getPaintingsByArtist(
            @Path("id") UUID artistId,
            @Query("page") int page,
            @Query("size") int size,
            @Query("sort") @Nullable String sort,
            @Header("Authorization") @Nullable String bearerToken);

    @GET("api/painting/{id}")
    Call<PaintingJson> getPaintingById(
            @Path("id") @Nonnull UUID paintingId,
            @Header("Authorization") @Nullable String bearerToken);

    @POST("api/painting")
    Call<PaintingJson> createPainting(
            @Body @Nonnull PaintingJson paintingJson,
            @Header("Authorization") @Nullable String bearerToken);

    @PATCH("api/painting")
    Call<PaintingJson> updatePainting(
            @Body @Nonnull PaintingJson paintingJson,
            @Header("Authorization") @Nullable String bearerToken);

    // Методы для работы с художниками

    @GET("api/artist")
    Call<ArtistPageResponse> getAllArtists(
            @Query("name") @Nullable String name,
            @Query("size") int size,
            @Query("page") int page,
            @Query("sort") @Nullable String sort,
            @Header("Authorization") @Nullable String bearerToken
    );

    @GET("api/artist/{id}")
    Call<ArtistJson> getArtistById(
            @Path("id") @Nonnull UUID id,
            @Header("Authorization") @Nullable String bearerToken
    );

    @POST("api/artist")
    Call<ArtistJson> addArtist(
            @Body @Nonnull ArtistJson artistJson,
            @Header("Authorization") @Nullable String bearerToken
    );

    @PATCH("bearerToken")
    Call<ArtistJson> updateArtist(
            @Body @Nonnull ArtistJson artistJson,
            @Header("Authorization") @Nullable String bearerToken
    );

    // Метод для работы со странами
    @GET("api/country")
    Call<CountryPageResponse> getCountries(
            @Query("Size") int size,
            @Query("page") int page,
            @Header("Authorization") @Nullable String bearerToken
    );

    // Методы для работы с музеями

    @GET("/api/museum")
    Call<MuseumPageResponse> getAllMuseum(
            @Query("name") @Nullable String name,
            @Query("page") int page,
            @Query("size") int size,
            @Query("sort") @Nullable String sort,
            @Header("Authorization") @Nullable String bearerToken
    );

    @GET("/api/museum/{id}")
    Call<MuseumJson> getMuseumById(
            @Path("id") @Nonnull UUID id,
            @Header("Authorization") @Nullable String bearerToken
    );

    @POST("/api/museum")
    Call<MuseumJson> addMuseum(
            @Body @Nonnull MuseumJson museumJson,
            @Header("Authorization") @Nullable String bearerToken
    );

    @PATCH("/api/museum")
    Call<MuseumJson> updateMuseum(
            @Body @Nonnull MuseumJson museumJson,
            @Header("Authorization") @Nullable String bearerToken
    );
}
