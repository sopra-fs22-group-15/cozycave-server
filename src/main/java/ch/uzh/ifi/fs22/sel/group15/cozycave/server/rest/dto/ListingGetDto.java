package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class ListingGetDto implements Serializable {

    private UUID id;

    private Date creationDate;

    private String name;

    private String description;

    private Location address;

    private Boolean published;

    private List<Picture> pictures;

    private int sqm;

    private ListingType listingtype;

    private Boolean furnished;

    private double rent;

    private double deposit;

    private int rooms;

    private User publisher;


    public ListingGetDto() {

    }

    public ListingGetDto(UUID id, Date creationDate, String name, String description,
                         Location address, Boolean published, Picture picture,
                         int sqm, ListingType listingtype, Boolean furnished, Gender availableTo,
                         Boolean available, double rent, double deposit, int rooms, User publisher) {
        this.id = id;
        this.creationDate = creationDate;
        this.name = name;
        this.description = description;
        this.address = address;
        this.published = published;
        this.pictures = pictures;
        this.sqm = sqm;
        this.listingtype = listingtype;
        this.furnished = furnished;
        this.rent = rent;
        this.deposit = deposit;
        this.rooms = rooms;
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

    public Boolean getPublished() {
        return published;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public int getSqm() {
        return sqm;
    }

    public ListingType getListingtype() {
        return listingtype;
    }

    public Boolean getFurnished() {
        return furnished;
    }

    public double getRent() {
        return rent;
    }

    public double getDeposit() {
        return deposit;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public User getPublisher() {
        return publisher;
    }
    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }
}
