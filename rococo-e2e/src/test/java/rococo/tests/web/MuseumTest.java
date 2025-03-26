package rococo.tests.web;


import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rococo.config.Config;
import rococo.jupiter.annotation.ApiLogin;
import rococo.jupiter.annotation.Museum;
import rococo.jupiter.annotation.ScreenShotTest;
import rococo.jupiter.annotation.User;
import rococo.jupiter.annotation.meta.WebTest;
import rococo.model.CountryJson;
import rococo.model.GeoJson;
import rococo.model.MuseumJson;
import rococo.model.UserJson;
import rococo.pageobject.MainPage;
import rococo.utils.RandomDataUtils;
import rococo.utils.ScreenDiffResult;

import java.awt.image.BufferedImage;
import java.io.IOException;

@WebTest
public class MuseumTest {

    private final static Config CFG = Config.getInstance();

    @Description("Поиск музея по названию без авторизации в системе")
    @Test
    @Museum
    void searchMuseumNotAuthorize(MuseumJson museum) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openMuseumPage()
                .searchMuseumByName(museum.title());
    }

    @Description("Просмотр карточки музея без авторизации. Проверка отсутствия кнопки редактирования")
    @Test
    @Museum
    void openMuseumCardNotAuthorize(MuseumJson museum) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openMuseumPage()
                .searchMuseumByName(museum.title())
                .openMuseumCard(museum.title())
                .checkVisibleEditMuseumInCardButton(false);
    }

    @Description("Проверка отсутствия кнопки 'Добавить музей' для неавторизованного пользователя")
    @Test
    void checkNotVisibleButtonAddMuseumNotAuthorize() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openMuseumPage()
                .checkVisibleAddMuseumButton(false);
    }

    @Description("Создание нового музея авторизованным пользователем")
    @Test
    @User
    @ApiLogin
    void addMuseum() {
        final String title = RandomDataUtils.randomName();
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openMuseumPage()
                .openCardToAddMuseum()
                .addMuseum(new MuseumJson(
                        null,
                        title,
                        RandomDataUtils.randomSentence(5),
                        new GeoJson(
                                RandomDataUtils.randomCategoryName(),
                                null
                        ),
                        "src/test/resources/uploadPicture/museum.jpg"

                ))
                .saveNewData()
                .searchMuseumByName(title);
    }

    @Description("Обновление фотографии музея авторизованным пользователем")
    @Test
    @Museum
    @User
    @ApiLogin
    @ScreenShotTest(value = "uploadPicture/expected_newMuseum.jpg")
    void editMuseumPhoto(MuseumJson museum, BufferedImage expected) throws IOException, InterruptedException {
        BufferedImage result = Selenide.open(CFG.frontUrl(), MainPage.class)
                .openMuseumPage()
                .searchMuseumByName(museum.title())
                .openMuseumCard(museum.title())
                .openCardToUpdateMuseum()
                .editPhotoMuseum("src/test/resources/uploadPicture/newMuseum.jpg")
                .saveUpdateData()
                .closeMessageAlert()
                .museumPhotoScreenshot();

        Assertions.assertFalse(new ScreenDiffResult(
                expected,
                result
        ), "Screen comparison failure");

    }

    @Description("Изменение названия музея с проверкой сохранения данных")
    @Test
    @Museum
    @User
    @ApiLogin
    void editMuseumTitle(MuseumJson museum) {
        final String title = RandomDataUtils.randomName();
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openMuseumPage()
                .searchMuseumByName(museum.title())
                .openMuseumCard(museum.title())
                .openCardToUpdateMuseum()
                .editTitleMuseum(title)
                .saveUpdateData()
                .closeMessageAlert()
                .checkNameMuseumInCard(title);
    }

    @Description("Обновление страны расположения музея")
    @Test
    @Museum
    @User
    @ApiLogin
    void editMuseumCountry(MuseumJson museum) {
        String result = Selenide.open(CFG.frontUrl(), MainPage.class)
                .openMuseumPage()
                .searchMuseumByName(museum.title())
                .openMuseumCard(museum.title())
                .openCardToUpdateMuseum()
                .editCountry()
                .saveUpdateData()
                .closeMessageAlert()
                .getCountryAndCityName();

        result = result.substring(0, result.indexOf(","));

        Assertions.assertNotEquals(museum.geo().country().name(), result);
    }

    @Description("Изменение города расположения музея (хардкод: Тула)")
    @Test
    @Museum
    @User
    @ApiLogin
    void editMuseumCity(MuseumJson museum) {
        final String city = "Тула";
        String result = Selenide.open(CFG.frontUrl(), MainPage.class)
                .openMuseumPage()
                .searchMuseumByName(museum.title())
                .openMuseumCard(museum.title())
                .openCardToUpdateMuseum()
                .editCity(city)
                .saveUpdateData()
                .closeMessageAlert()
                .getCountryAndCityName();

        result = result.substring(result.indexOf(",") + 2);

        Assertions.assertEquals(city, result);

    }

    @Description("Редактирование описания музея с генерацией случайного текста")
    @Test
    @Museum
    @User
    @ApiLogin
    void editMuseumDescription(MuseumJson museum) {
        final String description = RandomDataUtils.randomSentence(5);
        String result = Selenide.open(CFG.frontUrl(), MainPage.class)
                .openMuseumPage()
                .searchMuseumByName(museum.title())
                .openMuseumCard(museum.title())
                .openCardToUpdateMuseum()
                .editDescriptionMuseum(description)
                .saveUpdateData()
                .closeMessageAlert()
                .getDescriptionText();

        Assertions.assertEquals(description, result);
    }


    @Description("Проверка валидации: попытка сохранения музея с пустым названием")
    @Test
    @User
    @ApiLogin
    void checkValidationInputTitle() {

        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openMuseumPage()
                .openCardToAddMuseum()
                .addMuseum(new MuseumJson(
                        null,
                        "",
                        RandomDataUtils.randomSentence(5),
                        new GeoJson(
                                RandomDataUtils.randomCategoryName(),
                                null
                        ),
                        "src/test/resources/uploadPicture/museum.jpg"

                ))
                .clickSave()
                .checkButtonSaveVisible();
    }


    @Description("Проверка валидации: попытка сохранения музея с пустым описанием")
    @Test
    @User
    @ApiLogin
    void checkValidationInputDescription() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openMuseumPage()
                .openCardToAddMuseum()
                .addMuseum(new MuseumJson(
                        null,
                        RandomDataUtils.randomName(),
                        "",
                        new GeoJson(
                                RandomDataUtils.randomCategoryName(),
                                null
                        ),
                        "src/test/resources/uploadPicture/museum.jpg"

                ))
                .clickSave()
                .checkButtonSaveVisible();
    }


    @Description("Проверка валидации: попытка сохранения музея с пустым городом")
    @Test
    @User
    @ApiLogin
    void checkValidationInputCity() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openMuseumPage()
                .openCardToAddMuseum()
                .addMuseum(new MuseumJson(
                        null,
                        RandomDataUtils.randomName(),
                        RandomDataUtils.randomSentence(5),
                        new GeoJson(
                                "",
                                null
                        ),
                        "src/test/resources/uploadPicture/museum.jpg"

                ))
                .clickSave()
                .checkButtonSaveVisible();
    }


    @Description("Проверка валидации: попытка сохранения музея без загруженного изображения")
    @Test
    @User
    @ApiLogin
    void checkValidationInputPhoto() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openMuseumPage()
                .openCardToAddMuseum()
                .addMuseum(new MuseumJson(
                        null,
                        RandomDataUtils.randomName(),
                        RandomDataUtils.randomSentence(5),
                        new GeoJson(
                                RandomDataUtils.randomCategoryName(),
                                null
                        ),
                        ""

                ))
                .clickSave()
                .checkButtonSaveVisible();
    }

    @Description("Проверка валидации: попытка сохранения музея без выбранной страны")
    @Test
    @User
    @ApiLogin
    void checkValidationInputCountry() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openMuseumPage()
                .openCardToAddMuseum()
                .addMuseum(new MuseumJson(
                        null,
                        RandomDataUtils.randomName(),
                        RandomDataUtils.randomSentence(5),
                        new GeoJson(
                                RandomDataUtils.randomCategoryName(),
                                new CountryJson(null, null)
                        ),
                        "src/test/resources/uploadPicture/museum.jpg"

                ))
                .clickSave()
                .checkButtonSaveVisible();
    }

}
