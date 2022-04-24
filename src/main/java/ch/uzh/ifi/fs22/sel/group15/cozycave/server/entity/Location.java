package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "street")
    private String street;

    @Column(name = "street_number", nullable = false, length = 10)
    private String streetNumber;

    @Column(name = "zip_code", nullable = false)
    private int zipCode;

    @Column(name = "village", nullable = false)
    private String village;

    @Column(name = "country", nullable = false)
    private String country;

    public Location() {
    }

    public Location(String name, String street, String streetNumber, int zipCode, String village, String country) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.street = street;
        this.streetNumber = streetNumber;
        this.zipCode = zipCode;
        this.village = village;
        this.country = country;
    }

    public Location(String street, String streetNumber, int zipCode, String village, String country) {
        this.id = UUID.randomUUID();
        this.street = street;
        this.streetNumber = streetNumber;
        this.zipCode = zipCode;
        this.village = village;
        this.country = country;
    }

    public Location(UUID id, String name, String description, String street, String streetNumber, int zipCode,
        String village, String country) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.street = street;
        this.streetNumber = streetNumber;
        this.zipCode = zipCode;
        this.village = village;
        this.country = country;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override public String toString() {
        return "Location{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", street='" + street + '\'' +
            ", streetNumber='" + streetNumber + '\'' +
            ", zipCode=" + zipCode +
            ", village='" + village + '\'' +
            ", country='" + country + '\'' +
            '}';
    }
}