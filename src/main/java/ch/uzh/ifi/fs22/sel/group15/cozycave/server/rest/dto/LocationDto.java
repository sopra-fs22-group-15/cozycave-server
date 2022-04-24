package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;

// TODO: add @JsonProperty annotations
public class LocationDto implements Serializable {

    private String name;
    private String description;
    private String street;
    @JsonProperty("street_number")
    private String streetNumber;
    @JsonProperty("zip_code")
    private int zipCode;
    private String village;
    private String country;

    public LocationDto() {
    }

    public LocationDto(String name, String description, String street, String streetNumber, int zipCode,
        String village, String country) {
        this.name = name;
        this.description = description;
        this.street = street;
        this.streetNumber = streetNumber;
        this.zipCode = zipCode;
        this.village = village;
        this.country = country;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocationDto entity = (LocationDto) o;
        return Objects.equals(this.name, entity.name) &&
            Objects.equals(this.description, entity.description) &&
            Objects.equals(this.street, entity.street) &&
            Objects.equals(this.streetNumber, entity.streetNumber) &&
            Objects.equals(this.zipCode, entity.zipCode) &&
            Objects.equals(this.village, entity.village) &&
            Objects.equals(this.country, entity.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, street, streetNumber, zipCode, village, country);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
            "name = " + name + ", " +
            "description = " + description + ", " +
            "street = " + street + ", " +
            "streetNumber = " + streetNumber + ", " +
            "zipCode = " + zipCode + ", " +
            "village = " + village + ", " +
            "country = " + country + ")";
    }
}

