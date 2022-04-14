package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto;

import java.io.Serializable;
import java.util.Objects;

// TODO: add @JsonProperty annotations
public class LocationDto implements Serializable {

    private String name;
    private String description;
    private String street;
    private String streetNumber;
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

    public String getDescription() {
        return description;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getVillage() {
        return village;
    }

    public String getCountry() {
        return country;
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

