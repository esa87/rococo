package rococo.pageobject;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;

public class ProfilePage extends BasePage<ProfilePage> {

    private final SelenideElement firstnameInput = $("input[name='firstname']");
    private final SelenideElement lastnameInput = $("input[name='surname']");
    private final SelenideElement avatarInput = $("label input[type='file']");
    private final SelenideElement avatarPicture = $("form svg.avatar-initials");
    private final SelenideElement singoutButton = $("button.variant-ghost");
    private final SelenideElement closeButton = $("button.variant-ringed");
    private final SelenideElement updateButton = $("button[type='submit']");

    @Step("Редактируем имя пользователя")
    public ProfilePage editFirstname(String firstname) {
        firstnameInput.clear();
        firstnameInput.sendKeys(firstname);
        return this;
    }

    @Step("Редактируем фамилию пользователя")
    public ProfilePage editLastname(String lastname) {
        lastnameInput.clear();
        lastnameInput.sendKeys(lastname);
        return this;
    }

    @Step("Загружаем аватар")
    public ProfilePage uploadAvatar() {
        final File uploadFile = new File("src/test/resources/uploadPicture/avatar.png");
        avatarInput.sendKeys(uploadFile.getAbsolutePath());
        return this;
    }

    @Step("Выходим из учетной записи")
    public MainPage singOut() {
        singoutButton.click();
        return new MainPage();
    }

    @Step("Сохраняем изменения в данных пользователя")
    public MainPage updateUserData() {
        updateButton.click();
        return new MainPage();
    }

    @Step("Закрываем страницу профиля пользователя")
    public MainPage closeProfile() {
        closeButton.click();
        return new MainPage();
    }

    @Step("Получить текст из поля ввода имени")
    public String getTextFromFirstnameInput() {
        return firstnameInput.getValue();
    }

    @Step("Получить текст из поля ввода фамилии")
    public String getTextFromLastnameInput() {
        return lastnameInput.getValue();
    }

}
