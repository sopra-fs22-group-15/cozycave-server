package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "picture")
public class Picture {

    public static final String ROOT_PATH = "heroku-app.com/pictures/";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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


}