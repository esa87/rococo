package rococo.jupiter.extension;


import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import rococo.jupiter.annotation.Museum;
import rococo.model.CountryJson;
import rococo.model.GeoJson;
import rococo.model.MuseumJson;
import rococo.service.MuseumClient;
import rococo.service.grpc.MuseumGrpcClient;
import rococo.utils.RandomDataUtils;

import java.util.UUID;

import static rococo.utils.ConvertFile.encodeImageWithMime;


public class MuseumExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(MuseumExtension.class);

    private final MuseumClient museumClient = new MuseumGrpcClient();
    private final static String defaultCity = "Москва";
    private final static String defaultDescription = "Create by auto-test";


    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Museum.class)
                .ifPresent(museumAnno -> {
                    if ("".equals(museumAnno.title())) {
                        final String title = RandomDataUtils.randomName();
                        MuseumJson testMuseum = null;
                        try {
                            testMuseum = museumClient.addMuseum(
                                    new MuseumJson(
                                            null,
                                            title,
                                            defaultDescription,
                                            new GeoJson(
                                                    defaultCity,
                                                    new CountryJson(
                                                            null,
                                                            null
                                                    )
                                            ),
                                            encodeImageWithMime("rococo-e2e/src/test/resources/uploadPicture/museum.jpg")
                                    )
                            );
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        setMuseum(context, testMuseum);
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(MuseumJson.class);
    }

    @Override
    public MuseumJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getMuseumJson(extensionContext);
    }

    public static void setMuseum(ExtensionContext context, MuseumJson museumJson) {
        if (context == null) {
            throw new IllegalStateException("ExtensionContext is null.");
        }
        context.getStore(NAMESPACE).put(context.getUniqueId(), museumJson);
    }

    public static MuseumJson getMuseumJson(ExtensionContext context) {
        if (context == null) {
            throw new IllegalStateException("ExtensionContext is null.");
        }
        return context.getStore(NAMESPACE).get(context.getUniqueId(), MuseumJson.class);
    }
}