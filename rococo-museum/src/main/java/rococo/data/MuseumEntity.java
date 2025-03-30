package rococo.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.proxy.HibernateProxy;
import rococo.model.MuseumJson;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "museum")
public class MuseumEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")
            }
    )
    @Column(name = "id", columnDefinition = "BINARY(16) DEFAULT (UUID_TO_BIN(UUID(), true))", updatable = false, nullable = false)
    private UUID id;


    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "city")
    private String city;

    @Lob
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo;

    @Column(name = "country_id", nullable = false)
    private UUID countryId;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        MuseumEntity that = (MuseumEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public static MuseumEntity fromMuseumJson(MuseumJson museumJson){
        MuseumEntity entity = new MuseumEntity();
        entity.setId(museumJson.id());
        entity.setTitle(museumJson.title());
        entity.setDescription(museumJson.description());
        entity.setCity(museumJson.city());
        entity.setCountryId(museumJson.countryId());
        if (museumJson.photo() != null) {
            entity.setPhoto(museumJson.photo().getBytes(StandardCharsets.UTF_8));
        } else {
            entity.setPhoto(new byte[0]);
        };
        return entity;
    }

}
