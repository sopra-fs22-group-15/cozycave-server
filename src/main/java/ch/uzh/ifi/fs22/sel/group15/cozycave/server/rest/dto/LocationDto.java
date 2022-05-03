package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;

public class LocationDto implements Serializable {

    @JsonInclude(Include.NON_EMPTY)
    private String name;
    @JsonInclude(Include.NON_EMPTY)
    private String description;
    private String street;
    @JsonProperty("house_number")
    private String houseNumber;
    @JsonProperty("apartment_number")
    private String apartmentNumber;
    @JsonProperty("zip_code")
    private String zipCode;
    private String city;
    private String country;

    public LocationDto() {
    }

    public LocationDto(String name, String description, String street, String houseNumber, String apartmentNumber, String zipCode,
        String city, String country) {
        this.name = name;
        this.description = description;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
        this.zipCode = zipCode;
        this.city = city;
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

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
            Objects.equals(this.houseNumber, entity.houseNumber) &&
            Objects.equals(this.apartmentNumber, entity.apartmentNumber) &&
            Objects.equals(this.zipCode, entity.zipCode) &&
            Objects.equals(this.city, entity.city) &&
            Objects.equals(this.country, entity.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, street, houseNumber, apartmentNumber, zipCode, city, country);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
            "name = " + name + ", " +
            "description = " + description + ", " +
            "street = " + street + ", " +
            "houseNumber = " + houseNumber + ", " +
            "apartmentNumber = " + apartmentNumber + ", " +
            "zipCode = " + zipCode + ", " +
            "village = " + city + ", " +
            "country = " + country + ")";
    }
}

