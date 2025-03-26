package rococo.jupiter.extension;


import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import rococo.jupiter.annotation.Painting;
import rococo.model.*;
import rococo.service.ArtistClient;
import rococo.service.MuseumClient;
import rococo.service.PaintingClient;
import rococo.service.grpc.ArtistGrpcClient;
import rococo.service.grpc.MuseumGrpcClient;
import rococo.service.grpc.PaintingGrpcClient;
import rococo.utils.RandomDataUtils;

import java.util.UUID;

import static rococo.utils.ConvertFile.encodeImageWithMime;


public class PaintingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(PaintingExtension.class);

    private final MuseumClient museumClient = new MuseumGrpcClient();
    private final ArtistClient artistClient = new ArtistGrpcClient();
    private final PaintingClient paintingClient = new PaintingGrpcClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Painting.class)
                .ifPresent(paintingAnno -> {
                    if ("".equals(paintingAnno.title())) {
                        final String title = RandomDataUtils.randomName();
                        PaintingJson paintingJson = null;
                        try {
                            paintingJson = paintingClient.addPainting(
                                    new PaintingJson(
                                            null,
                                            title,
                                            paintingAnno.description(),
                                            artistClient.addArtist(
                                                    new ArtistJson(
                                                            null,
                                                            paintingAnno.artist().name().equals("")
                                                                    ? RandomDataUtils.randomName()
                                                                    : paintingAnno.artist().name(),
                                                            paintingAnno.artist().biography(),
                                                            null
                                                    )

                                            ),
                                            museumClient.addMuseum(new MuseumJson(
                                                    null,
                                                    paintingAnno.museum().title().equals("")
                                                            ? RandomDataUtils.randomName()
                                                            : paintingAnno.museum().title(),
                                                    paintingAnno.museum().description(),
                                                    new GeoJson(
                                                            paintingAnno.museum().city(),
                                                            new CountryJson(
                                                                    UUID.fromString(paintingAnno.museum().countryId()),
                                                                    null

                                                            )
                                                    ),
                                                    null
                                            )),
                                            encodeImageWithMime("src/test/resources/uploadPicture/mishki.jpg")
                                    )
                            );
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        setPainting(context, paintingJson);
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(PaintingJson.class);
    }

    @Override
    public PaintingJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getPaintingJson(extensionContext);
    }

    public static void setPainting(ExtensionContext context, PaintingJson paintingJson) {
        if (context == null) {
            throw new IllegalStateException("ExtensionContext is null.");
        }
        context.getStore(NAMESPACE).put(context.getUniqueId(), paintingJson);
    }

    public static PaintingJson getPaintingJson(ExtensionContext context) {
        if (context == null) {
            throw new IllegalStateException("ExtensionContext is null.");
        }
        return context.getStore(NAMESPACE).get(context.getUniqueId(), PaintingJson.class);
    }


}