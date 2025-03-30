package rococo.jupiter.extension;


import rococo.jupiter.annotation.User;
import rococo.service.UserClient;
import rococo.service.rest.UserRestClient;
import rococo.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import rococo.model.UserJson;


public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);

    private final UserClient usersClient = new UserRestClient();
    private static final String defaultPassword = "12345";

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if ("".equals(userAnno.username())) {
                        final String username = RandomDataUtils.randomUsername();
                        UserJson testUser = usersClient.createUser(username, defaultPassword);
                        setUser(context,testUser);
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getUserJson(extensionContext);
    }

    public static void setUser(ExtensionContext context, UserJson testUser) {
        if (context == null) {
            throw new IllegalStateException("ExtensionContext is null.");
        }
        context.getStore(NAMESPACE).put(context.getUniqueId(), testUser);
    }

    public static UserJson getUserJson(ExtensionContext context) {
        if (context == null) {
            throw new IllegalStateException("ExtensionContext is null.");
        }
        return context.getStore(NAMESPACE).get(context.getUniqueId(), UserJson.class);
    }
}