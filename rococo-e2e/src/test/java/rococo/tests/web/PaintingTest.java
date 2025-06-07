package rococo.tests.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rococo.api.GatewayApiClient;
import rococo.config.Config;
import rococo.jupiter.annotation.*;
import rococo.jupiter.annotation.meta.WebTest;
import rococo.model.ArtistJson;
import rococo.model.MuseumJson;
import rococo.model.PaintingJson;
import rococo.model.UserJson;
import rococo.pageobject.MainPage;
import rococo.utils.RandomDataUtils;
import rococo.utils.ScreenDiffResult;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Epic("Управление картинами")
@DisplayName("Тесты работы с картинами")
@WebTest
public class PaintingTest {
    private final static Config CFG = Config.getInstance();
    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();


    @Description("Проверка, что кнопка 'Добавить картину' не отображается для неавторизованных пользователей")
    @DisplayName("Неавторизованный пользователь: проверка видимости кнопки добавления")
    @Test
    void checkNotVisibleAddPaintingButtonThenNoAuthorize() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .checkVisibleAddPaintingButton(false);
    }

    @Description("Проверка, что кнопка 'Редактировать картину' не отображается для неавторизованных пользователей при поиске")
    @DisplayName("Неавторизованный пользователь: проверка видимости кнопки редактирования")
    @Painting
    @Test
    void checkNotVisibleEditPaintingButtonThenNoAuthorize(PaintingJson painting) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .searchPaintingByTitle(painting.title());
    }

    @Description("Проверка поиска картин для неавторизованных пользователей")
    @DisplayName("Неавторизованный пользователь: поиск картины")
    @Painting
    @Test
    void searchPaintingThenNoAuthorize(PaintingJson painting) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .searchPaintingByTitle(painting.title())
                .openPaintingCard(painting.title())
                .checkVisibleEditPaintingInCardButton(false);
    }

    @Description("Проверка поиска картин по части названия для авторизованного пользователя")
    @DisplayName("Поиск картины по части названия")
    @Painting
    @User
    @ApiLogin
    @Test
    void searchPaintingByPartTitle(PaintingJson painting, UserJson user) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .searchPaintingByTitle(painting.title().substring(2));
    }

    @Description("Проверка добавления новой картины авторизованным пользователем")
    @DisplayName("Добавление новой картины")
    @Artist
    @Museum
    @User
    @ApiLogin
    @Test
    void addPainting(ArtistJson artist, MuseumJson museum) {
        final String title = RandomDataUtils.randomName();
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .addPaintingCard()
                .addPainting(new PaintingJson(
                        null,
                        title,
                        RandomDataUtils.randomSentence(5),
                        artist,
                        museum,
                        "rococo-e2e/src/test/resources/uploadPicture/mishki.jpg"
                ))
                .saveNewData()
                .searchPaintingByTitle(title);
    }

    @Description("Проверка редактирования названия картины")
    @DisplayName("Редактирование названия картины")
    @Painting
    @User
    @ApiLogin
    @Test
    void editTitlePainting(PaintingJson painting) throws InterruptedException {
        String newTitle = RandomDataUtils.randomName();
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .searchPaintingByTitle(painting.title())
                .openPaintingCard(painting.title())
                .editPaintingCard()
                .editTitlePainting(newTitle)
                .editArtistPainting()
                .editMuseumPainting()
                .saveData()
                .checkTitlePaintingInCard(newTitle);
    }

    @Description("Проверка редактирования описания картины")
    @DisplayName("Редактирование описания картины")
    @Painting
    @User
    @ApiLogin
    @Test
    void editDescriptionPainting(PaintingJson painting) throws InterruptedException {
        String newDescription = RandomDataUtils.randomSentence(3);
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .searchPaintingByTitle(painting.title())
                .openPaintingCard(painting.title())
                .editPaintingCard()
                .editDescriptionPainting(newDescription)
                .editArtistPainting()
                .editMuseumPainting()
                .saveData()
                .checkDescriptionPaintingInCard(newDescription);
    }

    @Description("Проверка редактирования музея для картины")
    @DisplayName("Изменение музея для картины")
    @Painting
    @Museum
    @User
    @ApiLogin
    @Test
    void editMuseumPainting(PaintingJson painting, @Token String token) throws InterruptedException {

        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .searchPaintingByTitle(painting.title())
                .openPaintingCard(painting.title())
                .editPaintingCard()
                .editArtistPainting()
                .editMuseumPainting()
                .saveData();
        PaintingJson result = gatewayApiClient.getPaintingById(painting.id(), token);

        Assertions.assertNotEquals(painting.museum().title(), result.museum().title(), "Expected: "+painting.museum().title()+" actual: "+result.museum().title());
    }

    @Description("Проверка редактирования художника для картины")
    @DisplayName("Изменение художника для картины")
    @Painting
    @User
    @ApiLogin
    @Test
    void editArtistPainting(PaintingJson painting) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .searchPaintingByTitle(painting.title())
                .openPaintingCard(painting.title())
                .editPaintingCard()
                .editArtistPainting()
                .editMuseumPainting()
                .saveData()
                .getArtistValue(painting.artist().name());
    }

    @Description("Проверка изменения изображения картины")
    @DisplayName("Обновление изображения картины")
    @Test
    @Painting
    @User
    @ApiLogin
    @ScreenShotTest(value = "uploadPicture/expected_opyat_dvoyka.jpg", rewriteExpected = true)
    void editContentPainting(PaintingJson painting, BufferedImage expected) throws IOException, InterruptedException {
        BufferedImage actualPainting = Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .searchPaintingByTitle(painting.title())
                .openPaintingCard(painting.title())
                .editPaintingCard()
                .editArtistPainting()
                .editMuseumPainting()
                .editPhotoPainting("rococo-e2e/src/test/resources/uploadPicture/opyat_dvoyka.jpg")
                .saveData()
                .closeMessage()
                .avatarScreenshot();

        Assertions.assertFalse(new ScreenDiffResult(
                expected,
                actualPainting
        ), "Screen comparison failure");
    }

    @Description("Проверка отмены сохранения изменений картины")
    @DisplayName("Отмена изменений картины")
    @Test
    @Painting
    @User
    @ApiLogin
    @ScreenShotTest(value = "uploadPicture/expected_mishki.jpg", rewriteExpected = true)
    void changeNotSave(PaintingJson painting, BufferedImage expected) throws IOException, InterruptedException {
        BufferedImage actualPainting = Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .searchPaintingByTitle(painting.title())
                .openPaintingCard(painting.title())
                .editPaintingCard()
                .editPhotoPainting("rococo-e2e/src/test/resources/uploadPicture/opyat_dvoyka.jpg")
                .cancelSaveData()
                .avatarScreenshot();

        Assertions.assertFalse(new ScreenDiffResult(
                expected,
                actualPainting
        ), "Screen comparison failure");
    }

    @Description("Проверка валидации описания картины (менее 10 символов)")
    @DisplayName("Валидация: описание менее 10 символов")
    @Test
    @Painting
    @User
    @ApiLogin
    void checkDescriptionMoreTenChar(PaintingJson painting) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .searchPaintingByTitle(painting.title())
                .openPaintingCard(painting.title())
                .editPaintingCard()
                .editDescriptionPainting("123456789")
                .saveData()
                .checkVisibleDescriptionMessageError();
    }

    @Description("Проверка валидации названия картины (пустое значение)")
    @DisplayName("Валидация: пустое название")
    @Test
    @User
    @ApiLogin
    @Artist
    @Museum
    void checkValidationTitle(ArtistJson artist, MuseumJson museum) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .addPaintingCard()
                .addPainting(new PaintingJson(
                        null,
                        "",
                        RandomDataUtils.randomSentence(5),
                        artist,
                        museum,
                        "rococo-e2e/src/test/resources/uploadPicture/mishki.jpg"
                ))
                .saveWrongData()
                .checkButtonSaveIsVisible();

    }

    @Description("Проверка валидации описания картины (пустое значение)")
    @DisplayName("Валидация: пустое описание")
    @Test
    @User
    @ApiLogin
    @Artist
    @Museum
    void checkValidationDescription(ArtistJson artist, MuseumJson museum) {
        final String title = RandomDataUtils.randomName();
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .addPaintingCard()
                .addPainting(new PaintingJson(
                        null,
                        title,
                        "",
                        artist,
                        museum,
                        "rococo-e2e/src/test/resources/uploadPicture/mishki.jpg"
                ))
                .saveWrongData()
                .checkButtonSaveIsVisible();

    }

    @Description("Проверка валидации художника (не выбран художник)")
    @DisplayName("Валидация: не выбран художник")
    @Test
    @User
    @ApiLogin
    @Museum
    void checkValidationArtist(MuseumJson museum) throws InterruptedException {
        final String title = RandomDataUtils.randomName();
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .addPaintingCard()
                .addPainting(new PaintingJson(
                        null,
                        title,
                        RandomDataUtils.randomSentence(5),
                        null,
                        museum,
                        "rococo-e2e/src/test/resources/uploadPicture/mishki.jpg"
                ))
                .saveWrongData()
                .checkButtonSaveIsVisible();
    }

    @Description("Проверка валидации музея (не выбран музей)")
    @DisplayName("Валидация: не выбран музей")
    @Test
    @User
    @ApiLogin
    @Artist
    void checkValidationMuseum(ArtistJson artist) throws InterruptedException {
        final String title = RandomDataUtils.randomName();
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .addPaintingCard()
                .addPainting(new PaintingJson(
                        null,
                        title,
                        RandomDataUtils.randomSentence(5),
                        artist,
                        null,
                        "rococo-e2e/src/test/resources/uploadPicture/mishki.jpg"
                ))
                .saveWrongData()
                .checkButtonSaveIsVisible();
    }

    @Description("Проверка валидации изображения (не загружено изображение)")
    @DisplayName("Валидация: сохранение картины без изображения")
    @Test
    @User
    @ApiLogin
    @Artist
    @Museum
    void checkValidationPicture(ArtistJson artist, MuseumJson museum) throws InterruptedException {
        final String title = RandomDataUtils.randomName();
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openPaintingPage()
                .addPaintingCard()
                .addPainting(new PaintingJson(
                        null,
                        title,
                        RandomDataUtils.randomSentence(5),
                        artist,
                        museum,
                        ""
                ))
                .saveWrongData()
                .checkButtonSaveIsVisible();
    }

}