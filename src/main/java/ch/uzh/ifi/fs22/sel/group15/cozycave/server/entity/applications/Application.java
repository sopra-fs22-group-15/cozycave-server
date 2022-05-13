package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.applications;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ApplicationStatus;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "applications")
@AllArgsConstructor @Getter @Setter @ToString @NoArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", updatable = false)
    private Date creationDate;

    //TODO: GroupApplication, UserApplication

    @OneToOne(cascade = CascadeType.PERSIST)
    @NotFound(
            action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_id")
    private @NotNull User applicant;

    @OneToOne(cascade = CascadeType.PERSIST)
    @NotFound(
            action = NotFoundAction.IGNORE)
    @JoinColumn(name = "listing_id")
    private @NotNull Listing listing;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status", nullable = false)
    private ApplicationStatus application_status;


}

