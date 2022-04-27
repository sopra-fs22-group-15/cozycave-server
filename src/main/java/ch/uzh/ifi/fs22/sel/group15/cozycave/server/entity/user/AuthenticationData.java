package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "authentication_data")
public class AuthenticationData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "salt", nullable = false, unique = true, updatable = false, length = 16)
    private String salt;

    public AuthenticationData() {
    }

    public AuthenticationData(UUID id, String email, String password, String salt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.salt = salt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override public String toString() {
        return "AuthenticationData{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", salt='" + salt + '\'' +
            '}';
    }
}
