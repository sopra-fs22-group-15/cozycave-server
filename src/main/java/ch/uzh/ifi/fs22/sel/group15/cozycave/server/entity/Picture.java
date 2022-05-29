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
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "pictures")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Picture {

    public static final String ROOT_PATH = "http://database.cozycave.ch/CozyCave/";
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