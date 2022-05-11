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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "users")
@AllArgsConstructor @Getter @Setter @ToString @RequiredArgsConstructor
public class User implements Cloneable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
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