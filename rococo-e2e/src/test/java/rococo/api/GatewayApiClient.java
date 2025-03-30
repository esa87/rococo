package rococo.api;

import io.qameta.allure.Step;
import retrofit2.Response;
import rococo.config.Config;
import rococo.model.ArtistJson;
import rococo.model.PaintingJson;
import rococo.model.page.PaintingPageResponse;
import rococo.service.RestClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GatewayApiClient extends RestClient {

    protected static final Config CFG = Config.getInstance();
    private final GatewayApi gatewayApi;

     public GatewayApiClient() {
        super(CFG.gatewayUrl());
         this.gatewayApi = create(GatewayApi.class);
     }

    // Методы для работы с художниками
    @Step("Получить художника по ID {id}")
    public ArtistJson getArtistById(@Nonnull UUID id,
                                    @Nonnull String bearerToken) {
        final Response<ArtistJson> response;
        try {
            response = gatewayApi.getArtistById(id, bearerToken).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    // Методы для работы с картинами
    @Step("Получить все картины [фильтр: {title}, страница: {page}, размер: {size}, сортировка: {sort}]")
    public List<PaintingJson> getAllPaintings(@Nullable String title,
                                              int page,
                                              int size,
                                              @Nullable String sort,
                                              @Nonnull String bearerToken) {
        final Response<PaintingPageResponse> response;
        try {
             response = gatewayApi
                    .getAllPaintings(title, page, size, sort, bearerToken)
                    .execute();

            assertEquals(200, response.code());
            return response.body().getContent();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    @Step("Получить картины художника {artistId} [страница: {page}, размер: {size}, сортировка: {sort}]")
    public List<PaintingJson> getPaintingsByArtist(@Nonnull UUID artistId,
                                                   int page,
                                                   int size,
                                                   @Nullable String sort,
                                                   @Nonnull String bearerToken) {
        final Response<PaintingPageResponse> response;
        try {
            response = gatewayApi.getPaintingsByArtist(artistId, page, size, sort, bearerToken).execute();
            assertEquals(200, response.code());
            return response.body().getContent();
        } catch (IOException e) {
            throw new AssertionError(e);
        }

    }

    @Step("Получить картину по ID {paintingId}")
    public PaintingJson getPaintingById(@Nonnull UUID paintingId,
                                        @Nonnull String bearerToken) {
        final Response<PaintingJson> response;
        try {
            response = gatewayApi.getPaintingById(paintingId, bearerToken).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Создать новую картину")
    public PaintingJson createPainting(@Nonnull PaintingJson paintingJson,
                                       @Nonnull String bearerToken) {
        final Response<PaintingJson> response;
        try {
            response = gatewayApi.createPainting(paintingJson, bearerToken).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
        return response.body();
    }

    @Step("Обновить картину")
    public PaintingJson updatePainting(@Nonnull PaintingJson paintingJson,
                                       @Nonnull String bearerToken) {
        final Response<PaintingJson> response;
        try {
            response = gatewayApi.updatePainting(paintingJson, bearerToken).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }


}
