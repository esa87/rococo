package rococo.pageobject;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage<RegisterPage> {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement submitButton = $("button.form__submit");
    private final SelenideElement singInButton = $("a.form__link");
    private final SelenideElement singInAfterRegisterButton = $("a.form__submit");
    private final SelenideElement userAlreadyExists = $("span.form__error");


    @Step("Регистрируем нового пользователя с логином  {username}")
    public RegisterPage registerNewUser(String username, String password) {
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        passwordSubmitInput.sendKeys(password);
        submitButton.click();
        return this;
    }

    @Step("Переходим на главную страницу")
    public MainPage openMainPage() {
        singInAfterRegisterButton.should(visible);
        singInAfterRegisterButton.click();
        return new MainPage();
    }

    @Step("Переходим на страницу авторизации")
    public LoginPage openLoginPage() {
        singInButton.click();
        return new LoginPage();
    }

    @Step("проверяем что пользователь уже зарегестрирован в системе")
    public RegisterPage checkUserAlreadyExists() {
        userAlreadyExists.should(visible);
        return this;
    }


}
