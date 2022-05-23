package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.Utils;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;

import java.util.*;
import javax.persistence.*;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "user_details")
@AllArgsConstructor @Getter @Setter @ToString @NoArgsConstructor
public class UserDetails implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private Date birthday;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "about", columnDefinition = "TEXT")
    private String about;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "picture_id")
    private Picture picture;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        UserDetails that = (UserDetails) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public UserDetails clone() {
        return new UserDetails(
            this.id,
            this.firstName,
            this.lastName,
            this.gender,
            (Date) this.birthday.clone(),
            this.address.clone(),
            phoneNumber,
            about,
            this.picture
        );
    }

    @PrePersist
    private void prePersist() {
        this.id = UUID.randomUUID();

        if (this.phoneNumber != null) {
            this.phoneNumber = Utils.stripPhoneNumber(this.phoneNumber);
        }
    }

    @PreUpdate
    private void preUpdate() {
        if (this.phoneNumber != null) {
            this.phoneNumber = Utils.stripPhoneNumber(this.phoneNumber);
        }
    }
}