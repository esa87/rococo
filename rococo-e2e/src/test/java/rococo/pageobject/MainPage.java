package rococo.pageobject;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import rococo.pageobject.component.Header;
import rococo.pageobject.component.MessageAlert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage> {

    private final SelenideElement paintingButton = $("main[id='page-content'] a[href='/painting']");
    private final SelenideElement artistButton = $("main[id='page-content'] a[href='/artist']");
    private final SelenideElement museumButton = $("main[id='page-content'] a[href='/museum']");

    private final Header header = new Header();
    public static final String URL = CFG.frontUrl();

    private final MessageAlert messageAlert = new MessageAlert();


    @Step("Переходим на страницу с картинами с главной страницы")
    public PaintingPage openPaintingPage() {
        paintingButton.click();
        return new PaintingPage();
    }

    @Step("Переходим на страницу с художниками с главной страницы ")
    public ArtistPage openArtistPage() {
        artistButton.click();
        return new ArtistPage();
    }

    @Step("Переходим на страницу с музеями с главной страницы ")
    public MuseumPage openMuseumPage() {
        museumButton.click();
        return new MuseumPage();
    }

    @Step("Проверяем, что пользователь авторизован")
    public MainPage checkAvatarButton() {
        header.checkVisibleAvatarButton();
        return this;
    }

    @Step("Проверяем, что пользователь не авторизован")
    public MainPage checkLoginButton() {
        header.checkVisibleButtonLogin();
        return this;
    }

    @Step("Нажимаем на кнопку 'Войти'")
    public LoginPage openLoginPage() {
        return header.openLogin();
    }

    @Step("Открываем профиль пользователя")
    public ProfilePage openUserProfile() {
        return header.openProfilePage();
    }

    @Step("Get screenshot of avatar picture")
    public BufferedImage avatarScreenshot() throws IOException, InterruptedException {
        Thread.sleep(5000);
        return header.avatarScreenshot();
    }

    @Step("Закрыть всплывающее сообщение в карточке музея")
    public MainPage closeMessageAlert() {
        messageAlert.closeMessage();
        return this;
    }
}