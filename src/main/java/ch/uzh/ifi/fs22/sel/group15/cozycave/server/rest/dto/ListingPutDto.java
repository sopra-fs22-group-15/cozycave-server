package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;

import java.io.Serializable;
import java.util.*;


public class ListingPutDto implements Serializable {

    private UUID id;

    private String name;

    private String description;

    private LocationDto address;

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

    //private UserGetDto publisher;


    public ListingPutDto() {

    }

    // Constructor for single picture
    public ListingPutDto(UUID id, String name, String description,
                         LocationDto address, boolean published, Picture picture,
                         double sqm, ListingType listingtype, boolean furnished, List<Gender> availableTo,
                         boolean available, double rent, double deposit, double rooms) {
        this.id = id;
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
        //this.publisher = publisher;
    }

    // Constructor for multiple Picture add
    public ListingPutDto(UUID id, String name, String description,
                         LocationDto address, boolean published, List<Picture> pictures,
                         double sqm, ListingType listingtype, boolean furnished, List<Gender> availableTo,
                         boolean available, double rent, double deposit, double rooms) {
        this.id = id;
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
        //this.publisher = publisher;
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

    public LocationDto getAddress() {
        return address;
    }

    public void setAddress(LocationDto address) {
        this.address = address;
    }

    public boolean getPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public List<Picture> getPictures() {
        return pictures != null ? Collections.unmodifiableList(pictures) : new ArrayList<Picture>();
    }

    // overloading methods to be able to add single pictures or collections of pictures
    public void addPicture(Picture picture) {
        this.pictures.add(picture);
    }

    public void addPicture(List<Picture> pictures) {
        this.pictures.addAll(pictures);
    }

    public void removePicture(Picture picture) {
        this.pictures.remove(picture);
    }

    public void removePicture(List<Picture> pictures) {
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

    public List<Gender> getAvailableTo() {
        return availableTo != null ? Collections.unmodifiableList(availableTo) : new ArrayList<Gender>();
    }

    public void setAvailableTo(List<Gender> availableTo) { this.availableTo = availableTo; }

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

    /*public UserGetDto getPublisher() {
        return publisher;
    }

    public void setPublisher(UserGetDto publisher) {
        this.publisher = publisher;
    }*/
}
