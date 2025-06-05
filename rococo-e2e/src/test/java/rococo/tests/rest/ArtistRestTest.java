package rococo.tests.rest;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rococo.api.GatewayApiClient;
import rococo.jupiter.annotation.ApiLogin;
import rococo.jupiter.annotation.Artist;
import rococo.jupiter.annotation.Token;
import rococo.jupiter.annotation.User;
import rococo.jupiter.annotation.meta.RestTest;
import rococo.model.ArtistJson;
import rococo.model.page.ArtistPageResponse;
import rococo.utils.RandomDataUtils;

import java.time.LocalDate;

@RestTest
@DisplayName("Тесты REST API для художников")
@Epic("Управление художниками")
public class ArtistRestTest {

    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();

    @Description("Проверка дублирования после изменения имени")
    @DisplayName("Изменение имени художника не должно создавать дубликат")
    @Artist
    @User
    @ApiLogin(setupBrowser = false)
    @Test
    void checkDuplicateAfterChangeName(ArtistJson artist, @Token String token) {
        String newName = RandomDataUtils.randomName();
        ArtistJson updatedArtist = new ArtistJson(
                artist.id(),
                newName,
                artist.biography(),
                artist.photo()
        );
        gatewayApiClient.updateArtist(updatedArtist, token);

        ArtistPageResponse artistsPage = gatewayApiClient.getAllArtist(newName, 10, 0, null, token);

        Assertions.assertEquals(1, artistsPage.getContent().size());
    }

    @Description("Проверка дублирования после изменения биографии")
    @DisplayName("Изменение биографии художника не должно создавать дубликат")
    @Artist
    @User
    @ApiLogin(setupBrowser = false)
    @Test
    void checkDuplicateAfterChangeBiography(ArtistJson artist, @Token String token) {
        String newBiography = RandomDataUtils.randomSentence(10);
        ArtistJson updatedArtist = new ArtistJson(
                artist.id(),
                artist.name(),
                newBiography,
                artist.photo()
        );
        gatewayApiClient.updateArtist(updatedArtist, token);

        ArtistPageResponse artistsPage = gatewayApiClient.getAllArtist(artist.name(), 10, 0, null, token);

        Assertions.assertEquals(1, artistsPage.getContent().size());
    }

    @Description("Проверка получения художника по ID")
    @DisplayName("Успешное получение художника по корректному ID")
    @Artist
    @User
    @ApiLogin(setupBrowser = false)
    @Test
    void getArtistByIdSuccess(ArtistJson artist, @Token String token) {
        ArtistJson foundArtist = gatewayApiClient.getArtistById(artist.id(), token);

        Assertions.assertEquals(artist.id(), foundArtist.id());
        Assertions.assertEquals(artist.name(), foundArtist.name());
    }

    @Description("Проверка создания нового художника")
    @DisplayName("Успешное создание нового художника")
    @User
    @ApiLogin(setupBrowser = false)
    @Test
    void createArtistSuccess(@Token String token) {
        ArtistJson newArtist = new ArtistJson(
                null,
                RandomDataUtils.randomName(),
                RandomDataUtils.randomSentence(15),
                null
        );

        ArtistJson createdArtist = gatewayApiClient.addArtist(newArtist, token);

        Assertions.assertNotNull(createdArtist.id());
        Assertions.assertEquals(newArtist.name(), createdArtist.name());
    }

}