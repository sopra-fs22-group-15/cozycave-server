package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.application;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ApplicationStatus;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.UserDetails;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", updatable = false)
    private Date creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "applicationstatus", nullable = false)
    private ApplicationStatus applicationstatus;

    //TODO: GroupApplication, UserApplication

    @OneToOne(cascade = CascadeType.PERSIST, optional = false, orphanRemoval = true)
    @JoinColumn(name = "owner", nullable = false, unique = true)
    private User owner;


    public Application(UUID id, Date creationDate, ApplicationStatus applicationStatus, User owner)  {
        this.id = id;
        this.creationDate = creationDate;
        this.applicationstatus = applicationStatus;
        this.owner = owner;
    }
}
