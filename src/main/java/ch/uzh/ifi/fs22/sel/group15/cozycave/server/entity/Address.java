package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ADDRESS")
public class Address implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private int street_number;

    @Column(nullable = false)
    private int zip;


    @Column(nullable = false)
    private String village;


    @Column(nullable = false)
    private String country;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getStreetNumber() {
        return street_number;
    }

    public void setStreetNumber(int number) {
        this.street_number = number;
    }

    public int getZIP() {
        return zip;
    }

    public void setZIP(int zip) {
        this.zip = zip;
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

}
