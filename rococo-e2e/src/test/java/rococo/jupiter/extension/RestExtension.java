package rococo.jupiter.extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import rococo.service.ThreadSafeCookieStore;

public class RestExtension implements AfterEachCallback {
    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        ThreadSafeCookieStore.INSTANCE.removeAll();
    }
}
