package rococo.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import rococo.api.*;
import rococo.config.Config;
import rococo.jupiter.annotation.ApiLogin;
import rococo.jupiter.annotation.Token;
import rococo.model.*;
import rococo.model.UserJson;
import rococo.pageobject.MainPage;
import rococo.service.ThreadSafeCookieStore;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.openqa.selenium.Cookie;
import rococo.service.grpc.UserdataGrpcClient;

public class ApiLoginExtension implements BeforeTestExecutionCallback, ParameterResolver {
    private static final Config CFG = Config.getInstance();
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ApiLoginExtension.class);

    private final AuthClientRest authClientRest = new AuthClientRest();
    private final UserdataGrpcClient userdataApiClient = new UserdataGrpcClient();

    private final boolean setupBrowser;

    private ApiLoginExtension(boolean setupBrowser) {
        this.setupBrowser = setupBrowser;
    }

    public ApiLoginExtension() {
        this.setupBrowser = true;
    }

    public static ApiLoginExtension rest() {
        return new ApiLoginExtension(false);
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ApiLogin.class)
                .ifPresent(apiLogin -> {

                    final UserJson userToLogin;
                    final UserJson userFromUserExtension = UserExtension.getUserJson(context);
                    if ("".equals(apiLogin.username()) || "".equals(apiLogin.password())) {
                        if (userFromUserExtension == null) {
                            throw new IllegalStateException("@User must be present in case that @ApiLogin is empty!");
                        }
                        userToLogin = userFromUserExtension;
                    } else {
                        UserJson fakeUser = new UserJson(
                                apiLogin.username(),
                                new TestData(
                                        apiLogin.password()
                                )
                        );
                        if (userFromUserExtension != null) {
                            throw new IllegalStateException("@User must not be present in case that @ApiLogin contains username or password!");
                        }
                        UserExtension.setUser(context,fakeUser);
                        userToLogin = fakeUser;
                    }

                    final String token = authClientRest.getToken(
                            userToLogin.username(),
                            userToLogin.testData().password()
                    );
                    setToken(token);
                    if (setupBrowser) {
                        Selenide.open(CFG.frontUrl());
                        Selenide.localStorage().setItem("id_token", getToken());
                        WebDriverRunner.getWebDriver().manage().addCookie(
                                getJsessionIdCookie()
                        );
                        Selenide.open(MainPage.URL, MainPage.class).checkAvatarButton();
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(String.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), Token.class);
    }

    @Override
    public String resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return "Bearer " + getToken();
    }

    public static void setToken(String token) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("token", token);
    }

    public static String getToken() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("token", String.class);
    }

    public static void setCode(String code) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("code", code);
    }

    public static String getCode() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("code", String.class);
    }

    public static Cookie getJsessionIdCookie() {
        return new Cookie(
                "JSESSIONID",
                ThreadSafeCookieStore.INSTANCE.cookieValue("JSESSIONID")
        );
    }
}
