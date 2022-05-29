package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import java.util.Date;
import java.util.Objects;
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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@Builder
public class User implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", updatable = false, nullable = false)
    private Date creationDate;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "authentication_data_id", nullable = false, unique = true, updatable = false)
    private @NotNull AuthenticationData authenticationData;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "details_id", nullable = false, unique = true, updatable = false)
    private @NotNull UserDetails details;

    public User(UUID id, Date creationDate, @NotNull AuthenticationData authenticationData, Role role,
                @NotNull UserDetails details) {
        this.id = id;
        this.creationDate = creationDate;
        this.authenticationData = authenticationData;
        this.role = role;
        this.details = details;
    }

    public User() {
        this.authenticationData = new AuthenticationData();
        this.details = new UserDetails();
    }

    public void setAuthenticationData(@NotNull AuthenticationData authenticationData) {
        this.authenticationData = authenticationData == null ? new AuthenticationData() : authenticationData;
    }

    public void setDetails(@NotNull UserDetails details) {
        this.details = details == null ? new UserDetails() : details;
    }

    public User clone() {
        return new User(
                this.id,
                this.creationDate,
                this.authenticationData.clone(),
                this.role,
                this.details.clone()
        );
    }

    @PrePersist
    private void prePersist() {
        this.id = UUID.randomUUID();
        this.creationDate = new Date();
    }
}