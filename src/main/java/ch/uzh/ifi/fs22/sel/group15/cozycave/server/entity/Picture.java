package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "pictures")
@AllArgsConstructor @Getter @Setter @NoArgsConstructor
public class Picture {

    public static final String ROOT_PATH = "http://database.imhof-lan.ch/CozyCave/";
    public static final String GRAVATAR_PATH = "https://www.gravatar.com/avatar/";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", updatable = false)
    private Date creationDate;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", nullable = false)
    private User uploader;

    @Column(name = "picture_url", nullable = false)
    private String pictureUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Picture picture = (Picture) o;
        return id != null && Objects.equals(id, picture.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Picture clone() {
        return new Picture(
                this.id,
                this.creationDate,
                this.uploader,
                this.pictureUrl
        );
    }

    @PrePersist
    private void prePersist() {
        this.id = UUID.randomUUID();
        this.creationDate = new Date();
    }
}