package rococo.service.grpc;

import grpc.rococo.*;
import rococo.model.ArtistJson;
import rococo.model.CountryJson;
import rococo.model.GeoJson;
import rococo.model.MuseumJson;
import rococo.service.ArtistClient;
import rococo.service.MuseumClient;

import java.util.UUID;

public class MuseumGrpcClient extends BaseGrpc implements MuseumClient {


    @Override
    public MuseumJson addMuseum(MuseumJson museum) {
        CountriesResponse countriesResponse = countryBlockingStub.allCountries(
                AllCountriesRequest.newBuilder()
                        .setPageable(Pageable.newBuilder()
                                .setPage(0)
                                .setSize(10)
                                .build())
                        .build()
        );
        MuseumResponse response = museumBlockingStub.addMuseum(
                MuseumRequest.newBuilder()
                        .setTitle(museum.title())
                        .setDescription(museum.description())
                        .setCity(museum.geo().city())
                        .setCountryId(countriesResponse.getCountries(1).getId())
                        .setPhoto(museum.photo() == null
                                ? ""
                                : museum.photo())
                        .build()
        );


        return new MuseumJson(
                UUID.fromString(response.getId()),
                response.getTitle(),
                response.getDescription(),
                new GeoJson(
                        response.getCity(),
                        new CountryJson(
                                UUID.fromString(countriesResponse.getCountries(1).getId()),
                                countriesResponse.getCountries(1).getName()

                        )
                ),

                response.getPhoto()
        );
    }
}
