package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listing.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;
import java.util.UUID;
import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "picture")
public class Picture {

    public static final String ROOT_PATH = "heroku-app.com/pictures/";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @OneToOne(cascade = CascadeType.PERSIST, optional = false, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User uploader;

    public Picture() {
    }

    public Picture(UUID id, User uploader) {
        this.id = id;
        this.uploader = uploader;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }

    public String getURL() {
        return ROOT_PATH + id + ".jpg";
    }

    @Override public String toString() {
        return "Picture{" +
            "id=" + id +
            ", uploader=" + uploader +
            '}';
    }
}