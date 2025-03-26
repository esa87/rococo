package rococo.tests.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

@WebTest
public class ArtistTest {

    private final static Config CFG = Config.getInstance();

    @Description("Создание нового художника с валидными данными и проверка его отображения в поиске")
    @Test
    @User
    @ApiLogin
    void addNewArtist(UserJson user) {
        ArtistJson artist = new ArtistJson(
                null,
                RandomDataUtils.randomUsername(),
                RandomDataUtils.randomSentence(10),
                "src/test/resources/uploadPicture/Shishkin_I_I.jpg"
        );
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openArtistPage()
                .openAddArtistCard()
                .addArtist(artist)
                .saveNewData()
                .searchArtistByName(artist.name());
    }

    @Description("Изменение имени существующего художника и проверка обновленных данных")
    @Test
    @User
    @ApiLogin
    @Artist
    void editArtistName(ArtistJson artist) {
        String newNameArtist = RandomDataUtils.randomUsername();
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openArtistPage()
                .searchArtistByName(artist.name())
                .openArtistCard(artist.name())
                .openEditArtistCard()
                .editNameArtist(newNameArtist)
                .saveUpdateData()
                .closeMessageAlert()
                .checkNameArtistInCard(newNameArtist);

    }

    @Description("Визуальная проверка обновления фотографии художника через сравнение скриншотов")
    @Test
    @User
    @ApiLogin
    @Artist
    @ScreenShotTest(value = "uploadPicture/expected_Polenov_V_D.jpg")
    void editArtistPhoto(ArtistJson artist, BufferedImage expected) throws IOException {
        BufferedImage result = Selenide.open(CFG.frontUrl(), MainPage.class)
                .openArtistPage()
                .searchArtistByName(artist.name())
                .openArtistCard(artist.name())
                .openEditArtistCard()
                .editPhotoArtist("src/test/resources/uploadPicture/Polenov_V_D.jpg")
                .saveUpdateData()
                .closeMessageAlert()
                .artistPhotoScreenshot();

        Assertions.assertFalse(new ScreenDiffResult(
                expected,
                result
        ), "Screen comparison failure");
    }

    @Description("Обновление биографии художника и проверка сохранения изменений")
    @Test
    @User
    @Artist
    @ApiLogin
    void editArtistBiography(ArtistJson artist) {
        String newBiography = RandomDataUtils.randomSentence(4);
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openArtistPage()
                .searchArtistByName(artist.name())
                .openArtistCard(artist.name())
                .openEditArtistCard()
                .editBiographyArtist(newBiography)
                .saveUpdateData()
                .closeMessageAlert()
                .checkValueBiography(newBiography);
    }


    @Description("Проверка, что кнопка создания художника не отображается для неавторизованных пользователей")
    @Test
    void checkNotAuthorizeNotVisibleCreateArtistButton() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openArtistPage()
                .checkVisibleAddArtistButton(false);

    }

    @Description("Проверка отсутствия кнопки редактирования художника для неавторизованного пользователя")
    @Test
    @Artist
    void checkNotAuthorizeNotVisibleUpdateArtistButton(ArtistJson artistJson) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openArtistPage()
                .searchArtistByName(artistJson.name())
                .openArtistCard(artistJson.name())
                .checkVisibleEditArtistInCardButton(false);

    }

    @Description("Проверка, что кнопка добавления картины скрыта для неавторизованных пользователей")
    @Test
    @Artist
    void checkNotAuthorizeNotVisibleAddPictureButton(ArtistJson artistJson) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openArtistPage()
                .searchArtistByName(artistJson.name())
                .openArtistCard(artistJson.name())
                .checkVisibleAddPaintingInCardButton(false);
    }


    @Description("Поиск художника по полному имени")
    @Test
    @Artist
    @User
    @ApiLogin
    void searchArtist(ArtistJson artist) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openArtistPage()
                .searchArtistByName(artist.name());
    }

    @Description("Поиск художника по части имени (проверка поисковой выдачи)")
    @Test
    @Artist
    @User
    @ApiLogin
    void searchArtistPartName(ArtistJson artist) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openArtistPage()
                .searchArtistByName(artist.name().substring(3));
    }

    @Description("Добавление новой картины к существующему художнику с проверкой создания")
    @Test
    @Artist
    @Museum
    @User
    @ApiLogin
    void addPainting(ArtistJson artist, MuseumJson museum) {
        String title = RandomDataUtils.randomName();
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openArtistPage()
                .searchArtistByName(artist.name())
                .openArtistCard(artist.name())
                .openPaintingForm()
                .addPainting(new PaintingJson(
                        null,
                        title,
                        RandomDataUtils.randomSentence(5),
                        artist,
                        museum,
                        "src/test/resources/uploadPicture/mishki.jpg"
                ))
                .saveDataForArtist()
                .closeMessageAlert()
                .checkPaintingIsCreate();
    }


    @Description("Проверка отображения всех картин художника (тест-заглушка)")
    @Test
    void checkVisibleAllPaintings() {
        Assertions.assertTrue(false);
    }

}
