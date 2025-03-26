package rococo.service.grpc;

import grpc.rococo.*;
import rococo.model.*;
import rococo.service.MuseumClient;
import rococo.service.PaintingClient;

import java.util.UUID;

public class PaintingGrpcClient extends BaseGrpc implements PaintingClient {

    @Override
    public PaintingJson addPainting(PaintingJson paintingJson) {
        PaintingResponse paintingResponse = paintingBlockingStub.addPainting(
                PaintingRequest.newBuilder()
                        .setTitle(paintingJson.title())
                        .setDescription(paintingJson.description())
                        .setArtistId(paintingJson.artist().id().toString())
                        .setMuseumId(paintingJson.museum().id().toString())
                        .setContent(paintingJson.content()==null?"":paintingJson.content())
                        .build()
        );

       return new PaintingJson(
               UUID.fromString(paintingResponse.getId()),
               paintingResponse.getTitle(),
               paintingResponse.getDescription(),
               paintingJson.artist(),
               paintingJson.museum(),
               paintingResponse.getContent()
       );
    }
}
