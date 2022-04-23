package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listing;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", updatable = false)
    private Date creationDate;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id")
    private Location address;

    @Column(name = "published")
    private Boolean published;

    @OneToOne
    @JoinColumn(name = "picture_id")
    private Picture picture;

    @Column(name = "sqm")
    private int sqm;

    @Column(name = "listingtype")
    private ListingType listingtype;

    @Column(name = "furnished")
    private Boolean furnished;

    @Column(name = "rent")
    private double rent;

    @Column(name = "deposit")
    private double deposit;

    public Listing(UUID id, Date creationDate, String name, String description, Location address, Boolean published, Picture picture,
                   int sqm, ListingType listingtype, Boolean furnished, double rent, double deposit) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.published = published;
        this.picture = picture;
        this.sqm = sqm;
        this.listingtype = listingtype;
        this.furnished = furnished;
        this.rent = rent;
        this.deposit = deposit;
    }

}
