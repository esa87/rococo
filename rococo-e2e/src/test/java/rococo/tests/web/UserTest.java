package rococo.tests.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rococo.config.Config;
import rococo.jupiter.annotation.ApiLogin;
import rococo.jupiter.annotation.ScreenShotTest;
import rococo.jupiter.annotation.User;
import rococo.jupiter.annotation.meta.WebTest;
import rococo.model.UserJson;
import rococo.pageobject.MainPage;
import rococo.utils.RandomDataUtils;
import rococo.utils.ScreenDiffResult;

import java.awt.image.BufferedImage;
import java.io.IOException;

@WebTest
@Epic("Управление профилем пользователя")
@DisplayName("Тесты управления профилем пользователя")
public class UserTest {

    private final static Config CFG = Config.getInstance();

    @Description("Проверка изменения имени пользователя в профиле")
    @DisplayName("Изменение имени пользователя")
    @Test
    @User
    @ApiLogin
    void editFirstname() {
        String firstname = RandomDataUtils.randomName();
        String result = Selenide.open(CFG.frontUrl(), MainPage.class)
                .openUserProfile()
                .editFirstname(firstname)
                .updateUserData()
                .openUserProfile()
                .getTextFromFirstnameInput();
        Assertions.assertEquals(firstname, result);
    }

    @Description("Проверка изменения фамилии пользователя в профиле")
    @DisplayName("Изменение фамилии пользователя")
    @Test
    @User
    @ApiLogin
    void editLastname(UserJson user) {
        String lastname = RandomDataUtils.randomName();
        String result = Selenide.open(CFG.frontUrl(), MainPage.class)
                .openUserProfile()
                .editLastname(lastname)
                .updateUserData()
                .openUserProfile()
                .getTextFromLastnameInput();
        Assertions.assertEquals(lastname, result);
    }

    @Description("Проверка загрузки и изменения аватара пользователя")
    @DisplayName("Обновление аватара пользователя")
    @Test
    @User
    @ApiLogin
    @ScreenShotTest(value = "uploadPicture/expected_avatar.png", rewriteExpected = true)
    void editAvatar(UserJson user, BufferedImage expected) throws IOException, InterruptedException {
        String firstname = RandomDataUtils.randomName();
        BufferedImage actualAvatar = Selenide.open(CFG.frontUrl(), MainPage.class)
                .openUserProfile()
                .uploadAvatar()
                .updateUserData()
                .closeMessageAlert()
                .avatarScreenshot();

        Assertions.assertFalse(new ScreenDiffResult(
                expected,
                actualAvatar
        ), "Screen comparison failure");
    }

    @Description("Проверка выхода пользователя из системы после авторизации")
    @DisplayName("Выход пользователя из системы")
    @Test
    @User
    @ApiLogin
    void logoutUser(UserJson user) {
        String firstname = RandomDataUtils.randomName();
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openUserProfile()
                .singOut()
                .checkLoginButton();
    }

}
