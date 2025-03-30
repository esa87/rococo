package rococo.tests.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rococo.config.Config;
import rococo.jupiter.annotation.User;
import rococo.jupiter.annotation.meta.WebTest;
import rococo.model.UserJson;
import rococo.pageobject.MainPage;

@WebTest
@Epic("Тестирование страницы авторизации")
@DisplayName("Тесты авторизации пользователя")
public class LoginTest {
    private static final Config CFG = Config.getInstance();

    @Description("Тест на корректные учетные данные")
    @DisplayName("Успешная авторизация с валидными данными")
    @User
    @Test
    public void correctLogin(UserJson user) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openLoginPage()
                .setAuthorizedData(user.username(), user.testData().password())
                .singIn()
                .checkAvatarButton();
    }

    @Description("Тест на некорректный логин")
    @DisplayName("Попытка авторизации с неверным логином")
    @User
    @Test
    public void incorrectLogin(UserJson user) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openLoginPage()
                .setAuthorizedData("qwe", user.testData().password())
                .singInWithWrongData()
                .checkWrongLoginMessage();
    }

    @Description("Тест на некорректный пароль")
    @DisplayName("Попытка авторизации с неверным паролем")
    @User
    @Test
    public void incorrectPassword(UserJson user) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openLoginPage()
                .setAuthorizedData(user.username(), "212")
                .singInWithWrongData()
                .checkWrongLoginMessage();
    }

}