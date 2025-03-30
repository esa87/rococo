package rococo.pageobject;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement singInButton = $("button.form__submit");
    private final SelenideElement registerLink = $("a[href='/register']");
    private final SelenideElement wrongAutorizeMessage = $("p.form__error");


    @Step("Водим учетные данные пользователя {username}")
    public LoginPage setAuthorizedData(String username, String password) {
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        return this;
    }

    @Step("Нажимаем кнопку войти")
    public MainPage singIn(){
        singInButton.click();
        return new MainPage();
    }

    @Step("Нажимаем кнопку войти если указали некорректный логин или пароль")
    public LoginPage singInWithWrongData(){
        singInButton.click();
        return this;
    }

    @Step("Переходим на страницу авторизации")
    public RegisterPage openRegisterPage() {
        registerLink.click();
        return new RegisterPage();
    }

    @Step("Проверяем наличие сообщения некорректного ввода учетных данных")
    public LoginPage checkWrongLoginMessage(){
        wrongAutorizeMessage.should(visible);
        return this;
    }
}
