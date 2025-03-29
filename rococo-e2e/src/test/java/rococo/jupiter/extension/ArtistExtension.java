package rococo.jupiter.extension;


import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import rococo.jupiter.annotation.Artist;
import rococo.model.ArtistJson;
import rococo.service.ArtistClient;
import rococo.service.grpc.ArtistGrpcClient;
import rococo.utils.RandomDataUtils;

import static rococo.utils.ConvertFile.encodeImageWithMime;


public class ArtistExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ArtistExtension.class);

    private final ArtistClient artistClient = new ArtistGrpcClient();


    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Artist.class)
                .ifPresent(artistAnno -> {
                    if ("".equals(artistAnno.name())) {
                        final String name = RandomDataUtils.randomUsername();
                        ArtistJson testArtist = null;
                        try {
                            testArtist = artistClient.addArtist(
                                    new ArtistJson(
                                            null,
                                            name,
                                            artistAnno.biography(),
                                            encodeImageWithMime("rococo-e2e/src/test/resources/uploadPicture/Shishkin_I_I.jpg")
                                    )
                            );
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        setArtist(context, testArtist);
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(ArtistJson.class);
    }

    @Override
    public ArtistJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getArtistJson(extensionContext);
    }

    public static void setArtist(ExtensionContext context, ArtistJson artistJson) {
        if (context == null) {
            throw new IllegalStateException("ExtensionContext is null.");
        }
        context.getStore(NAMESPACE).put(context.getUniqueId(), artistJson);
    }

    public static ArtistJson getArtistJson(ExtensionContext context) {
        if (context == null) {
            throw new IllegalStateException("ExtensionContext is null.");
        }
        return context.getStore(NAMESPACE).get(context.getUniqueId(), ArtistJson.class);
    }
}