package rococo.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.proxy.HibernateProxy;
import rococo.model.ArtistJson;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "artist")
@Access(AccessType.PROPERTY)
public class ArtistEntity {
    private UUID id;
    private String name;
    private String biography;
    private byte[] photo;

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
    public UUID getId() {
        return id;
    }

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    @Column(name = "biography", length = 2000, nullable = false)
    @Basic(fetch = FetchType.LAZY)
    public String getBiography() {
        return biography;
    }

    @Lob
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    public byte[] getPhoto() {
        return photo;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ArtistEntity that = (ArtistEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public static ArtistEntity fromArtistJson(ArtistJson artistJson){
        ArtistEntity entity = new ArtistEntity();
        entity.setId(artistJson.id());
        entity.setName(artistJson.name());
        entity.setBiography(artistJson.biography());
        if (artistJson.photo() != null) {
            entity.setPhoto(artistJson.photo().getBytes(StandardCharsets.UTF_8));
        } else {
            entity.setPhoto(new byte[0]);
        }
        return entity;
    }
}
