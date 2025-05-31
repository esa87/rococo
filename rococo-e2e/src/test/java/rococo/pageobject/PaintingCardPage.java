package rococo.pageobject;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import rococo.pageobject.component.Header;
import rococo.pageobject.component.MessageAlert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaintingCardPage extends BasePage<PaintingCardPage> {


    private final SelenideElement titlePaintingInCard = $("header.card-header");
    private final SelenideElement descriptionPaintingInCard = $("main[id='page-content'] div div.m-4");
    private final SelenideElement artistPaintingInCard = $("main[id='page-content'] div div.text-center");
    private final SelenideElement editPaintingInCardButton = $("button[data-testid='edit-painting']");
    private final SelenideElement descriptionErrorMsg = $("label.label span.text-error-400");
    private final SelenideElement paintingPhoto = $("img.mx-auto");

    private final MessageAlert messageAlert = new MessageAlert();
    private final Header header = new Header();

    @Step("Открыть форму редактирования картины")
    public EditPaintingPage editPaintingCard() {
        editPaintingInCardButton.click();
        return new EditPaintingPage();
    }

    @Step("Проверить, что название картины соответствует '{titlePainting}'")
    public PaintingCardPage checkTitlePaintingInCard(String titlePainting) throws InterruptedException {
        Thread.sleep(1000);
        String actualTitlePainting = titlePaintingInCard.getText();
        Assertions.assertEquals(titlePainting, actualTitlePainting);
        return this;
    }

    @Step("Проверить, что описание картины соответствует '{descriptionPainting}'")
    public PaintingCardPage checkDescriptionPaintingInCard(String descriptionPainting) throws InterruptedException {
        Thread.sleep(1000);
        String actualDescriptionPainting = descriptionPaintingInCard.getText();
        Assertions.assertEquals(descriptionPainting, actualDescriptionPainting);
        return this;
    }

    @Step("Проверить видимость кнопки редактирования картины (ожидаемо: {needVisibleButton})")
    public PaintingCardPage checkVisibleEditPaintingInCardButton(boolean needVisibleButton) {
        Assertions.assertEquals(
                needVisibleButton,
                editPaintingInCardButton.isDisplayed()
        );
        return this;
    }

    @Step("Проверить, что имя художника соответствует '{artistName}'")
    public PaintingCardPage getArtistValue(String artistName) throws InterruptedException {
        Thread.sleep(1000);
        String actualArtistName = artistPaintingInCard.getText();
        Assertions.assertEquals(artistName, actualArtistName);
        return this;
    }

    @Step("Проверить отображение ошибки валидации описания (минимум 10 символов)")
    public PaintingCardPage checkVisibleDescriptionMessageError() {
        descriptionErrorMsg.shouldBe(visible);
        return this;
    }

    @Step("Сделать скриншот изображения картины")
    public BufferedImage avatarScreenshot() throws IOException, InterruptedException {
        Thread.sleep(5000);
        return ImageIO.read(paintingPhoto.screenshot());
    }

    @Step("Закрыть всплывающее сообщение")
    public PaintingCardPage closeMessage() {
        messageAlert.closeMessage();
        return this;
    }

    @Step("Вернуться на главную страницу через шапку сайта")
    public MainPage returnToMainPage() {
        header.returnMainPage();
        return new MainPage();
    }

}
