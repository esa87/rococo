package rococo.tests.rest;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rococo.api.GatewayApiClient;
import rococo.jupiter.annotation.ApiLogin;
import rococo.jupiter.annotation.Museum;
import rococo.jupiter.annotation.Token;
import rococo.jupiter.annotation.User;
import rococo.jupiter.annotation.meta.RestTest;
import rococo.model.CountryJson;
import rococo.model.GeoJson;
import rococo.model.MuseumJson;
import rococo.model.page.MuseumPageResponse;
import rococo.utils.RandomDataUtils;

import java.util.UUID;

@RestTest
@DisplayName("Тесты REST API для музеев")
@Epic("Управление музеями")
public class MuseumRestTest {

    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();

    @Description("Проверка создания нового музея")
    @DisplayName("Успешное создание музея")
    @User
    @ApiLogin(setupBrowser = false)
    @Test
    void createMuseumSuccess(@Token String token) {
        MuseumJson newMuseum = new MuseumJson(
                null,
                "Музей изящных искусств " + RandomDataUtils.randomName(),
                "Описание музея " + RandomDataUtils.randomSentence(10),
                new GeoJson(
                        RandomDataUtils.randomCategoryName(),
                        new CountryJson(
                                UUID.fromString("11f0003d-5d72-84d7-969b-569bd7e2abf7"),
                                "Австрия"
                        )
                ),
                ""

        );

        MuseumJson createdMuseum = gatewayApiClient.addMuseum(newMuseum, token);

        Assertions.assertNotNull(createdMuseum.id());
        Assertions.assertEquals(newMuseum.title(), createdMuseum.title());
    }

    @Description("Проверка получения музея по ID")
    @DisplayName("Успешное получение музея по ID")
    @Museum
    @User
    @ApiLogin(setupBrowser = false)
    @Test
    void getMuseumByIdSuccess(MuseumJson museum, @Token String token) {
        MuseumJson foundMuseum = gatewayApiClient.getMuseumById(museum.id(), token);

        Assertions.assertEquals(museum.id(), foundMuseum.id());
        Assertions.assertEquals(museum.title(), foundMuseum.title());
    }

    @Description("Проверка обновления данных музея")
    @DisplayName("Успешное обновление названия музея")
    @Museum
    @User
    @ApiLogin(setupBrowser = false)
    @Test
    void updateMuseumTitleSuccess(MuseumJson museum, @Token String token) {
        String newTitle = "Обновленное название " + RandomDataUtils.randomName();
        MuseumJson updatedMuseum = new MuseumJson(
                museum.id(),
                newTitle,
                museum.description(),
                museum.geo(),
                museum.photo()
        );

        MuseumJson result = gatewayApiClient.updateMuseum(updatedMuseum, token);

        Assertions.assertEquals(newTitle, result.title());
    }

    @Description("Проверка поиска музеев по названию")
    @DisplayName("Фильтрация музеев по названию")
    @Museum
    @User
    @ApiLogin(setupBrowser = false)
    @Test
    void filterMuseumsByTitle(MuseumJson museum, @Token String token) {
        MuseumPageResponse response = gatewayApiClient.getAllMuseum(
                museum.title(), 0, 1, null, token);

        Assertions.assertTrue(response.getContent().stream()
                .anyMatch(m -> m.title().equals(museum.title())));
    }


    @Description("Проверка дублирования после изменения названия")
    @DisplayName("Изменение названия музея не должно создавать дубликат")
    @Museum
    @User
    @ApiLogin(setupBrowser = false)
    @Test
    void checkDuplicateAfterChangeTitle(MuseumJson museum, @Token String token) {
        String newTitle = "Новое название " + RandomDataUtils.randomName();
        MuseumJson updatedMuseum = new MuseumJson(
                museum.id(),
                newTitle,
                museum.description(),
                museum.geo(),
                museum.photo()
        );
        gatewayApiClient.updateMuseum(updatedMuseum, token);

        MuseumPageResponse response = gatewayApiClient.getAllMuseum(newTitle, 0, 10, null, token);

        Assertions.assertEquals(1, response.getContent().size());
    }

}
