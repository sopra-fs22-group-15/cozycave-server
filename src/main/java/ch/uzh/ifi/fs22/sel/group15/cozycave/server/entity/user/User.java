package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import java.util.Date;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", updatable = false)
    private Date creationDate;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "authentication_data_id")
    private AuthenticationData authenticationData;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "details_id", nullable = false, unique = true)
    private UserDetails details;

    public User() {
    }

    public User(UUID id, Date creationDate,
        AuthenticationData authenticationData, Role role,
        UserDetails details) {
        this.id = id;
        this.creationDate = creationDate;
        this.authenticationData = authenticationData;
        this.role = role;
        this.details = details;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public AuthenticationData getAuthenticationData() {
        return authenticationData;
    }

    public void setAuthenticationData(
        AuthenticationData authenticationData) {
        this.authenticationData = authenticationData;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserDetails getDetails() {
        return details;
    }

    public void setDetails(UserDetails details) {
        this.details = details;
    }

    @Override public String toString() {
        return "User{" +
            "id=" + id +
            ", creationDate=" + creationDate +
            ", authenticationData=" + authenticationData +
            ", role=" + role +
            ", details=" + details +
            '}';
    }
}