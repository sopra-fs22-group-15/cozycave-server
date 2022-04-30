package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


public class ListingGetDto implements Serializable {

    private UUID id;

    private Date creationDate;

    private String name;

    private String description;

    private Location address;

    private boolean published;

    private List<Picture> pictures;

    private double sqm;

    private ListingType listingtype;

    private boolean furnished;

    private List<Gender> availableTo;
    
    private boolean available;

    private double rent;

    private double deposit;

    private double rooms;

    private UUID publisherID;


    public ListingGetDto() {

    }

    // Constructor for single picture
    public ListingGetDto(UUID id, Date creationDate, String name, String description,
                         Location address, boolean published, Picture picture,
                         double sqm, ListingType listingtype, boolean furnished, List<Gender> availableTo,
                         boolean available, double rent, double deposit, double rooms, UUID publisherID) {
        this.id = id;
        this.creationDate = creationDate;
        this.name = name;
        this.description = description;
        this.address = address;
        this.published = published;
        this.pictures = new ArrayList<>();
        if (picture != null) {
            this.pictures.add(picture);
        }
        this.sqm = sqm;
        this.listingtype = listingtype;
        this.furnished = furnished;
        this.availableTo = new ArrayList<>();
        if (availableTo != null) {
            this.availableTo.addAll(availableTo);
        }
        this.available = available;
        this.rent = rent;
        this.deposit = deposit;
        this.rooms = rooms;
        this.publisherID = publisherID;
    }

    // Constructor for multiple Picture add
    public ListingGetDto(UUID id, Date creationDate, String name, String description,
                         Location address, boolean published, List<Picture> pictures,
                         double sqm, ListingType listingtype, boolean furnished, List<Gender> availableTo,
                         boolean available, double rent, double deposit, double rooms, UUID publisherID) {
        this.id = id;
        this.creationDate = creationDate;
        this.name = name;
        this.description = description;
        this.address = address;
        this.published = published;
        this.pictures = new ArrayList<>();
        if (pictures != null) {
            this.pictures.addAll(pictures);
        }
        this.sqm = sqm;
        this.listingtype = listingtype;
        this.furnished = furnished;
        this.availableTo = new ArrayList<>();
        if (availableTo != null) {
            this.availableTo.addAll(availableTo);
        }
        this.available = available;
        this.rent = rent;
        this.deposit = deposit;
        this.rooms = rooms;
        this.publisherID = publisherID;
    }

    public UUID getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Location getAddress() {
        return address;
    }

    public boolean getPublished() {
        return published;
    }

    public List<Picture> getPictures() {
        return pictures != null ? Collections.unmodifiableList(pictures) : new ArrayList<Picture>();
    }

    public double getSqm() {
        return sqm;
    }

    public ListingType getListingtype() {
        return listingtype;
    }

    public boolean getFurnished() {
        return furnished;
    }

    public List<Gender> getAvailableTo() {
        return availableTo != null ? Collections.unmodifiableList(availableTo) : new ArrayList<Gender>();
    }

    public boolean getAvailable() { return available; }

    public double getRent() {
        return rent;
    }

    public double getDeposit() {
        return deposit;
    }

    public double getRooms() {
        return rooms;
    }

    public void setRooms(double rooms) {
        this.rooms = rooms;
    }

    public UUID getPublisher() {
        return publisherID;
    }
    public void setPublisher(UUID publisherID) {
        this.publisherID = publisherID;
    }
}
