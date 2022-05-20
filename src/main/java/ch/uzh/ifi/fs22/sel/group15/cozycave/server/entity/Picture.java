package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "pictures")
@AllArgsConstructor @Getter @Setter @ToString @NoArgsConstructor
public class Picture {

    public static final String ROOT_PATH = "https://sopra-fs22-group-15-server.herokuapp.com/pictures/";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", updatable = false)
    private Date creationDate;

    @OneToOne(cascade = CascadeType.PERSIST, optional = false, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
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

    @PrePersist
    private void prePersist() {
        this.id = UUID.randomUUID();
        this.creationDate = new Date();
    }
}