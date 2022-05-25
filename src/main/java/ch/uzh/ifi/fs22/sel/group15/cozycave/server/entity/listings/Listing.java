package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.AssertionsUtils;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "listings")
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Listing implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", updatable = false, nullable = false)
    private Date creationDate;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id")
    private Location address;

    @Column(name = "published", nullable = false)
    private Boolean published;

    @Column(name = "sqm")
    private Double sqm;

    @Column(name = "listing_type")
    private ListingType listingType;

    @Column(name = "furnished")
    private Boolean furnished;

    @ElementCollection(targetClass = Gender.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "listings_gender")
    @Column(name = "available_to")
    private List<Gender> availableTo;

    @Column(name = "available")
    private Boolean available;

    @Column(name = "rent")
    private Double rent;

    @Column(name = "deposit")
    private Double deposit;

    @Column(name = "rooms")
    private Double rooms;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "publisher_id", nullable = false)
    private User publisher;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "picture_listing_id")
    private List<Picture> pictures = new ArrayList<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "floorplan_listing_id")
    private List<Picture> floorplan = new ArrayList<>();


    public boolean isReadyToPublish() {
        return StringUtils.hasText(title)
                && StringUtils.hasText(getDescription())
                && getAddress() != null && getAddress().isValid()
                && getListingType() != null
                && !AssertionsUtils.isEmpty(getAvailableTo())
                && getAvailable() != null && getAvailable()
                && getPublisher() != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Listing listing = (Listing) o;
        return id != null && Objects.equals(id, listing.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Listing clone() {
        return new Listing(
                this.getId(),
                this.getCreationDate(),
                this.getTitle(),
                this.getDescription(),
                this.getAddress(),
                this.getPublished(),
                this.getSqm(),
                this.getListingType(),
                this.getFurnished(),
                List.copyOf(this.getAvailableTo()),
                this.getAvailable(),
                this.getRent(),
                this.getDeposit(),
                this.getRooms(),
                this.getPublisher(),
                this.getPictures(),
                this.getFloorplan()
        );
    }
}
