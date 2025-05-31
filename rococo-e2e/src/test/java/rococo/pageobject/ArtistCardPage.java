package rococo.pageobject;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import rococo.pageobject.component.MessageAlert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ArtistCardPage extends BasePage<ArtistCardPage>{

    private final SelenideElement nameArtistInCard = $("header.card-header");
    private final SelenideElement editArtistInCardButton = $("button[data-testid='edit-artist']");
    private final SelenideElement addPaintingInCardButton = $("section div.flex button.variant-filled-primary");
    private final SelenideElement biographyField = $("p.col-span-2");
    private final SelenideElement artistPhoto = $("img.avatar-image");
    private final ElementsCollection listPaintings = $$("section div ul li a");


    private final MessageAlert messageAlert = new MessageAlert();

    @Step("Открываем карточку на редактирование художника")
    public EditArtistPage openEditArtistCard() {
        editArtistInCardButton.click();
        return new EditArtistPage();
    }

    @Step("Проверяем, что название художника соответствует ожидаемому: {nameArtist}")
    public ArtistCardPage checkNameArtistInCard(String nameArtist) {
        String actualNameArtist = nameArtistInCard.getText();
        Assertions.assertEquals(nameArtist, actualNameArtist);
        return this;
    }

    @Step("Проверяем отображение кнопки редактирования художника (ожидаемо: {needVisibleButton})")
    public ArtistCardPage checkVisibleEditArtistInCardButton(boolean needVisibleButton) {
        Assertions.assertEquals(
                needVisibleButton,
                editArtistInCardButton.isDisplayed()
        );
        return this;
    }

    @Step("Проверяем отображение кнопки 'Добавить картину' (ожидаемо: {needVisibleButton})")
    public ArtistCardPage checkVisibleAddPaintingInCardButton(boolean needVisibleButton) {
        Assertions.assertEquals(
                needVisibleButton,
                addPaintingInCardButton.isDisplayed()
        );
        return this;
    }

    @Step("Проверяем соответствие текста в поле биографии (ожидаемо: {expectedBiography})")
    public ArtistCardPage checkValueBiography(String expectedBiography) {
        final String actualBiography = biographyField.getText();
        Assertions.assertEquals(expectedBiography, actualBiography);
        return this;
    }

    @Step("Закрываем всплывающее сообщение")
    public ArtistCardPage closeMessageAlert() {
        messageAlert.closeMessage();
        return this;
    }

    @Step("Делаем скриншот фотографии художника")
    public BufferedImage artistPhotoScreenshot() throws IOException {
        return ImageIO.read(artistPhoto.shouldBe(visible).screenshot());
    }

    @Step("Открываем форму добавления новой картины")
    public EditPaintingPage openPaintingForm() {
        addPaintingInCardButton.click();
        return new EditPaintingPage();
    }

    @Step("Проверяем, что хотя бы одна картина создана")
    public ArtistCardPage checkPaintingIsCreate() {
        Assertions.assertTrue(listPaintings.size() > 0);
        return this;
    }

}
