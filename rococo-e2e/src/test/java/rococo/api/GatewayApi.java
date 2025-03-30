package rococo.api;

import retrofit2.Call;
import retrofit2.http.*;
import rococo.model.ArtistJson;
import rococo.model.PaintingJson;
import rococo.model.page.PaintingPageResponse;

import java.util.List;
import java.util.UUID;

public interface GatewayApi {

    // Методы для работы с картинами

    @GET("api/painting")
    Call<PaintingPageResponse> getAllPaintings(
            @Query("title") String title,
            @Query("page") int page,
            @Query("size") int size,
            @Query("sort") String sort,
            @Header("Authorization") String bearerToken);

    @GET("api/painting/author/{id}")
    Call<PaintingPageResponse> getPaintingsByArtist(
            @Path("id") UUID artistId,
            @Query("page") int page,
            @Query("size") int size,
            @Query("sort") String sort,
            @Header("Authorization") String bearerToken);

    @GET("api/painting/{id}")
    Call<PaintingJson> getPaintingById(
            @Path("id") UUID paintingId,
            @Header("Authorization") String bearerToken);

    @POST("api/painting")
    Call<PaintingJson> createPainting(
            @Body PaintingJson paintingJson,
            @Header("Authorization") String bearerToken);

    @PATCH("api/painting")
    Call<PaintingJson> updatePainting(
            @Body PaintingJson paintingJson,
            @Header("Authorization") String bearerToken);

    // Метод для работы с художниками
    @GET("api/artist/{id}")
    Call<ArtistJson> getArtistById(
            @Path("id") UUID id,
            @Header("Authorization") String bearerToken);


}
