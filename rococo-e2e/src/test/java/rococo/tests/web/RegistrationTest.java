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
import rococo.utils.RandomDataUtils;

@WebTest
@DisplayName("Тесты регистрации пользователей")
@Epic("Регистрация пользователя")
public class RegistrationTest {

    private final static Config CFG = Config.getInstance();

    @Description("роверка успешной регистрации нового пользователя и последующей авторизации")
    @DisplayName("Успешная регистрация нового пользователя")
    @Test
    void createNewUser() {
        String username = RandomDataUtils.randomUsername();
        String password = "123";
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openLoginPage()
                .openRegisterPage()
                .registerNewUser(username, password)
                .openMainPage()
                .openLoginPage()
                .setAuthorizedData(username, password)
                .singIn()
                .checkAvatarButton();
    }

    @Description("Проверка попытки регистрации уже существующего пользователя")
    @DisplayName("Попытка регистрации существующего пользователя")
    @Test
    @User
    void createAlreadyUser(UserJson user) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openLoginPage()
                .openRegisterPage()
                .registerNewUser(user.username(), user.testData().password())
                .checkUserAlreadyExists();

    }
}
