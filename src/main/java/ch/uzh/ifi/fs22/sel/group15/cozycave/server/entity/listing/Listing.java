package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listing;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;
import com.google.common.collect.ImmutableList;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "listings")
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
    private boolean published;

    //@OneToMany(targetEntity=Picture.class, mappedBy="id", fetch=FetchType.EAGER)
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "picture_id")
    private List<Picture> pictures;

    @Column(name = "sqm")
    private double sqm;

    @Column(name = "listingtype")
    private ListingType listingtype;


    @Column(name = "furnished")
    private boolean furnished;

    //TODO: change to have multiple options
    @Enumerated(EnumType.STRING)
    private Gender availableTo;

    @Column(name = "available")
    private boolean available;

    @Column(name = "rent")
    private double rent;

    @Column(name = "deposit")
    private double deposit;

    @Column(name = "rooms")
    private double rooms;

    @OneToOne
    @JoinColumn(name = "publisher_id")
    private User publisher;

    public Listing() {

    }

    // Constructor for single picture
    public Listing(UUID id, Date creationDate, String name, String description,
                   Location address, boolean published, Picture picture,
                   double sqm, ListingType listingtype, boolean furnished, Gender availableTo,
                   boolean available, double rent, double deposit, double rooms, User publisher) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.published = published;
        this.pictures = new ArrayList<>();
        this.pictures.add(picture);
        this.sqm = sqm;
        this.listingtype = listingtype;
        this.furnished = furnished;
        this.availableTo = availableTo;
        this.available = available;
        this.rent = rent;
        this.deposit = deposit;
        this.rooms = rooms;
        this.publisher = publisher;
    }

    // Constructor for multiple Picture add
    public Listing(UUID id, Date creationDate, String name, String description,
                   Location address, boolean published, List<Picture> pictures,
                   double sqm, ListingType listingtype, boolean furnished, Gender availableTo,
                   boolean available, double rent, double deposit, double rooms, User publisher) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.published = published;
        this.pictures = new ArrayList<>();
        this.pictures.addAll(pictures);
        this.sqm = sqm;
        this.listingtype = listingtype;
        this.furnished = furnished;
        this.availableTo = availableTo;
        this.available = available;
        this.rent = rent;
        this.deposit = deposit;
        this.rooms = rooms;
        this.publisher = publisher;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getAddress() {
        return address;
    }

    public void setAddress(Location address) {
        this.address = address;
    }

    public boolean getPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public List<Picture> getPictures() {
        return Collections.unmodifiableList(pictures);
    }

    public void addPicture(Picture picture) {
        this.pictures.add(picture);
    }

    public void addPictures(List<Picture> pictures) {
        this.pictures.addAll(pictures);
    }

    public void removePicture(Picture picture) {
        this.pictures.remove(picture);
    }

    public void removePictures(List<Picture> pictures) {
        this.pictures.removeAll(pictures);
    }


    public double getSqm() {
        return sqm;
    }

    public void setSqm(double sqm) {
        this.sqm = sqm;
    }

    public ListingType getListingtype() {
        return listingtype;
    }

    public void setListingtype(ListingType listingtype) {
        this.listingtype = listingtype;
    }

    public boolean getFurnished() {
        return furnished;
    }

    public void setFurnished(boolean furnished) {
        this.furnished = furnished;
    }

    public Gender getAvailableTo() { return availableTo; }

    public void setAvailableTo(Gender availableTo) { this.availableTo = availableTo; }

    public boolean getAvailable() { return available; }

    public void setAvailable(boolean available) { this.available = available; }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getRooms() {
        return rooms;
    }

    public void setRooms(double rooms) {
        this.rooms = rooms;
    }

    public User getPublisher() {
        return publisher;
    }
    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }
}
