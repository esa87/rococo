package rococo.pageobject;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import rococo.pageobject.component.MessageAlert;
import rococo.pageobject.component.SearchBlock;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MuseumCardPage extends BasePage<MuseumCardPage> {


    private final SelenideElement nameMuseumInCard = $("header.card-header");
    private final SelenideElement editMuseumInCardButton = $("button[data-testid='edit-museum']");
    private final SelenideElement museumPicture = $("#page-content img");
    private final SelenideElement countryAndCityName = $("div.text-center");
    private final SelenideElement descriptionText = $("#page-content div div:last-child");

    private final MessageAlert messageAlert = new MessageAlert();

    @Step("Открываем карточку для редактировании данных о музеи")
    public EditMuseumPage openCardToUpdateMuseum() {
        editMuseumInCardButton.click();
        return new EditMuseumPage();
    }

    @Step("Проверяем что название музея соответствует ожидаемому")
    public MuseumCardPage checkNameMuseumInCard(String namePainting) {
        String actualNameMuseum = nameMuseumInCard.getText();
        Assertions.assertEquals(namePainting, actualNameMuseum);
        return this;
    }

    @Step("Проверяем отображение кнопки редактирования музея, если состояния авторизации пользователя = {needVisibleButton}")
    public MuseumCardPage checkVisibleEditMuseumInCardButton(boolean needVisibleButton) {
        Assertions.assertEquals(
                needVisibleButton,
                editMuseumInCardButton.isDisplayed()
        );
        return this;
    }

    @Step("Закрыть всплывающее сообщение в карточке музея")
    public MuseumCardPage closeMessageAlert() {
        messageAlert.closeMessage();
        return this;
    }

    @Step("Сделать скриншот изображения музея для визуальной проверки")
    public BufferedImage museumPhotoScreenshot() throws IOException {
        return ImageIO.read(museumPicture.shouldBe(visible).screenshot());
    }

    @Step("Получить текст с названием страны и города музея")
    public String getCountryAndCityName() {
        return countryAndCityName.getText();
    }

    @Step("Получить текст описания музея")
    public String getDescriptionText() {
        return descriptionText.getText();
    }
}
