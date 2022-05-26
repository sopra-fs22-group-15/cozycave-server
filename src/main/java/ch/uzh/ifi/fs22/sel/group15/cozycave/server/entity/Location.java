package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.Transient;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "location")
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Location implements Cloneable {

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

    @Column(name = "house_number", nullable = false, length = 10)
    private String houseNumber;

    @Column(name = "apartment_number", length = 10)
    private String apartmentNumber;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country", nullable = false)
    private String country;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Location location = (Location) o;
        return id != null && Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // TODO: check if address exists in real world
    @Transient
    public boolean isValid() {
        return StringUtils.hasText(street)
                && StringUtils.hasText(houseNumber)
                && StringUtils.hasText(zipCode)
                && StringUtils.hasText(city)
                && StringUtils.hasText(country);
    }

    public Location clone() {
        return new Location(
                this.id,
                this.name,
                this.description,
                this.street,
                this.houseNumber,
                this.apartmentNumber,
                this.zipCode,
                this.city,
                this.state,
                this.country
        );
    }

    @PrePersist
    private void prePersist() {
        this.id = UUID.randomUUID();
    }
}