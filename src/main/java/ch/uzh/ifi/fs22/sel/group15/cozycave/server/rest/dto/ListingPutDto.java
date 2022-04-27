package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class ListingPutDto implements Serializable {

    private UUID id;

    private String name;

    private String description;

    private Location address;

    private Boolean published;

    private List<Picture> pictures;

    private int sqm;

    private ListingType listingtype;

    private Boolean furnished;

    private Gender availableTo;

    private Boolean available;

    private double rent;

    private double deposit;

    private int rooms;

    private User publisher;


    public ListingPutDto() {

    }

    public ListingPutDto(UUID id, String name, String description,
                         Location address, Boolean published, Picture picture,
                         int sqm, ListingType listingtype, Boolean furnished, Gender availableTo,
                         Boolean available, double rent, double deposit, int rooms, User publisher) {
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

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public List<Picture> getPictures() {
        List<Picture> temp = new ArrayList<>();
        for (int i = 0; i < pictures.size(); i++) {
            temp.add(pictures.get(i));
        }
        return temp;
    }

    public void addPictures(Picture picture) {
        this.pictures.add(picture);
    }

    public void addPictures(List<Picture> pictures) {
        for (int i = 0; i < pictures.size(); i++) {
            this.pictures.add(pictures.get(i));
        }
    }

    public int getSqm() {
        return sqm;
    }

    public void setSqm(int sqm) {
        this.sqm = sqm;
    }

    public ListingType getListingtype() {
        return listingtype;
    }

    public void setListingtype(ListingType listingtype) {
        this.listingtype = listingtype;
    }

    public Boolean getFurnished() {
        return furnished;
    }

    public void setFurnished(Boolean furnished) {
        this.furnished = furnished;
    }

    public Gender getAvailableTo() { return availableTo; }

    public void setAvailableTo(Gender availableTo) { this.availableTo = availableTo; }

    public Boolean getAvailable() { return available; }

    public void setAvailable(Boolean available) { this.available = available; }

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
