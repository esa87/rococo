package rococo.pageobject.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import rococo.pageobject.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {
    public Header() {
        super($("[id='shell-header']"));
    }

    @Step("Переходим на страницу картинами из заголовка")
    public PaintingPage openPaintingPage() {
        self.$("a[href='/painting']").click();
        return new PaintingPage();
    }

    @Step("Переходим на страницу художниками из заголовка")
    public ArtistPage openArtistPage() {
        self.$("a[href='/artist']").click();
        return new ArtistPage();
    }

    @Step("Переходим на страницу музеями из заголовка")
    public MuseumPage openMuseumPage() {
        self.$("a[href='/museum']").click();
        return new MuseumPage();
    }

    @Step("Переходим на страницу профиля пользователя")
    public ProfilePage openProfilePage() {
        self.$("button.btn-icon").click();
        return new ProfilePage();
    }

    @Step("Меняем тему дизайна сайта")
    public ProfilePage changeMode() {
        self.$("div.lightswitch-thumb").click();
        return new ProfilePage();
    }

    @Step("Переходим на страницу авторизации")
    public LoginPage openLogin() {
        self.$("button.variant-filled-primary").click();
        return new LoginPage();
    }

    @Step("Проверить, что кнопка/иконка аватара отображается на странице")
    public MainPage checkVisibleAvatarButton() {
        self.$("figure.avatar").should(visible);
        return new MainPage();
    }

    @Step("Проверить, что кнопка войти отображается на странице")
    public MainPage checkVisibleButtonLogin() {
        self.$("button.variant-filled-primary").should(visible);
        return new MainPage();
    }

    @Step("Get screenshot of avatar picture")
    public BufferedImage avatarScreenshot() throws IOException {
        return ImageIO.read(new Header().self.$("figure.avatar").screenshot());
    }

    @Step("Переходим на страницу авторизации")
    public MainPage returnMainPage() {
        self.$(" a[href='/']").click();
        return new MainPage();
    }




}
