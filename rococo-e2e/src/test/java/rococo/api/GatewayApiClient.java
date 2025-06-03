package rococo.api;

import io.qameta.allure.Step;
import retrofit2.Response;
import rococo.config.Config;
import rococo.model.ArtistJson;
import rococo.model.MuseumJson;
import rococo.model.PaintingJson;
import rococo.model.page.ArtistPageResponse;
import rococo.model.page.CountryPageResponse;
import rococo.model.page.MuseumPageResponse;
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
    @Step("Получить список художников с параметрами: "
            + "name='{name}', size={size}, page={page}, sort='{sort}'")
    public ArtistPageResponse getAllArtist(
            @Nullable String name,
            int size,
            int page,
            @Nullable String sort,
            @Nullable String bearerToken) {
        final Response<ArtistPageResponse> response;
        try {
            response = gatewayApi.getAllArtists(name, size, page, sort, bearerToken).execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при выполнении запроса получения списка художников", e);
        }
        assertEquals(200, response.code(), "Неверный HTTP-статус ответа");
        return response.body();
    }

    @Step("Получить художника по ID {id}")
    public ArtistJson getArtistById(@Nonnull UUID id,
                                    @Nullable String bearerToken) {
        final Response<ArtistJson> response;
        try {
            response = gatewayApi.getArtistById(id, bearerToken).execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при получении художника с ID: " + id, e);
        }
        assertEquals(200, response.code(), "Неверный HTTP-статус ответа");
        return response.body();
    }

    @Step("Добавить нового художника: {artistJson.name}")
    public ArtistJson addArtist(@Nonnull ArtistJson artistJson,
                                @Nullable String bearerToken) {
        final Response<ArtistJson> response;
        try {
            response = gatewayApi.addArtist(artistJson, bearerToken).execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при добавлении художника: " + artistJson.name(), e);
        }
        assertEquals(200, response.code(), "Неверный HTTP-статус ответа");
        return response.body();
    }

    @Step("Обновить данные художника: {artistJson.name} (ID: {artistJson.id})")
    public ArtistJson updateArtist(@Nonnull ArtistJson artistJson,
                                   @Nullable String bearerToken) {
        final Response<ArtistJson> response;
        try {
            response = gatewayApi.updateArtist(artistJson, bearerToken).execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при обновлении художника с ID: " + artistJson.id(), e);
        }
        assertEquals(200, response.code(), "Неверный HTTP-статус ответа");
        return response.body();
    }


    // Методы для работы с картинами
    @Step("Получить список картин. Параметры: "
            + "фильтр по названию: {title}, "
            + "страница #{page}, "
            + "размер страницы: {size}, "
            + "сортировка: {sort}")
    public List<PaintingJson> getAllPaintings(@Nullable String title,
                                              int page,
                                              int size,
                                              @Nullable String sort,
                                              @Nullable String bearerToken) {
        final Response<PaintingPageResponse> response;
        try {
            response = gatewayApi
                    .getAllPaintings(title, page, size, sort, bearerToken)
                    .execute();

            assertEquals(200, response.code(), "Ожидался статус код 200");
            return response.body().getContent();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при получении списка картин", e);
        }
    }

    @Step("Получить картины художника {artistId}. "
            + "Параметры: страница #{page}, "
            + "размер страницы: {size}, "
            + "сортировка: {sort}")
    public List<PaintingJson> getPaintingsByArtist(@Nonnull UUID artistId,
                                                   int page,
                                                   int size,
                                                   @Nullable String sort,
                                                   @Nullable String bearerToken) {
        final Response<PaintingPageResponse> response;
        try {
            response = gatewayApi.getPaintingsByArtist(artistId, page, size, sort, bearerToken)
                    .execute();
            assertEquals(200, response.code(), "Ожидался статус код 200");
            return response.body().getContent();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при получении картин художника " + artistId, e);
        }
    }

    @Step("Получить детали картины с ID {paintingId}")
    public PaintingJson getPaintingById(@Nonnull UUID paintingId,
                                        @Nullable String bearerToken) {
        final Response<PaintingJson> response;
        try {
            response = gatewayApi.getPaintingById(paintingId, bearerToken).execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при получении картины с ID " + paintingId, e);
        }
        assertEquals(200, response.code(), "Ожидался статус код 200");
        return response.body();
    }

    @Step("Создать новую картину: {paintingJson.title}")
    public PaintingJson createPainting(@Nonnull PaintingJson paintingJson,
                                       @Nullable String bearerToken) {
        final Response<PaintingJson> response;
        try {
            response = gatewayApi.createPainting(paintingJson, bearerToken).execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при создании картины '" + paintingJson.title() + "'", e);
        }
        assertEquals(201, response.code(), "Ожидался статус код 201 (Created)");
        return response.body();
    }

    @Step("Обновить картину {paintingJson.id}. Новые данные: "
            + "название: {paintingJson.title}, "
            + "художник: {paintingJson.artistId}")
    public PaintingJson updatePainting(@Nonnull PaintingJson paintingJson,
                                       @Nullable String bearerToken) {
        final Response<PaintingJson> response;
        try {
            response = gatewayApi.updatePainting(paintingJson, bearerToken).execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при обновлении картины " + paintingJson.id(), e);
        }
        assertEquals(200, response.code(), "Ожидался статус код 200");
        return response.body();
    }

    // Метод для работы со странами

    @Step("Получить список стран. Параметры: страница #{page}, размер страницы: {size}")
    public CountryPageResponse getAllCountry(
            int size,
            int page,
            @Nullable String bearerToken) {
        final Response<CountryPageResponse> response;
        try {
            response = gatewayApi.getCountries(size, page, bearerToken).execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при получении списка стран (страница " + page + ", размер " + size + ")", e);
        }
        assertEquals(200, response.code(), "Ожидался HTTP-статус 200 OK");
        return response.body();
    }

    // Методы для работы с музеями

    @Step("Получить список музеев. Параметры: "
            + "название: {name}, "
            + "страница #{page}, "
            + "размер страницы: {size}, "
            + "сортировка: {sort}")
    public MuseumPageResponse getAllMuseum(
            @Nullable String name,
            int size,
            int page,
            @Nullable String sort,
            @Nullable String bearerToken) {
        final Response<MuseumPageResponse> response;
        try {
            response = gatewayApi.getAllMuseum(name, size, page, sort, bearerToken).execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при получении списка музеев с параметрами: "
                    + "name=" + name + ", page=" + page + ", size=" + size + ", sort=" + sort, e);
        }
        assertEquals(200, response.code(), "Ожидался HTTP-статус 200 OK");
        return response.body();
    }

    @Step("Получить музей по ID {id}")
    public MuseumJson getMuseumById(@Nonnull UUID id,
                                    @Nullable String bearerToken) {
        final Response<MuseumJson> response;
        try {
            response = gatewayApi.getMuseumById(id, bearerToken).execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при получении музея с ID: " + id, e);
        }
        assertEquals(200, response.code(), "Ожидался HTTP-статус 200 OK");
        return response.body();
    }

    @Step("Добавить новый музей: {museumJson.title}")
    public MuseumJson addMuseum(@Nonnull MuseumJson museumJson,
                                @Nullable String bearerToken) {
        final Response<MuseumJson> response;
        try {
            response = gatewayApi.addMuseum(museumJson, bearerToken).execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при добавлении музея '" + museumJson.title() + "'", e);
        }
        assertEquals(201, response.code(), "Ожидался HTTP-статус 201 Created");
        return response.body();
    }

    @Step("Обновить данные музея. ID: {museumJson.id}, новое название: {museumJson.title}")
    public MuseumJson updateMuseum(@Nonnull MuseumJson museumJson,
                                   @Nullable String bearerToken) {
        final Response<MuseumJson> response;
        try {
            response = gatewayApi.updateMuseum(museumJson, bearerToken).execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при обновлении музея ID: " + museumJson.id()
                    + ", название: '" + museumJson.title() + "'", e);
        }
        assertEquals(200, response.code(), "Ожидался HTTP-статус 200 OK");
        return response.body();
    }


}
