package models;

import models.security.User;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.List;

@Table(name = "TRAVELLER")
@Entity
public class Traveller extends Model {
    @Column(name = "FIRST_NAME", length = 80)
    public String firstName;

    @Column(name = "LAST_NAME", length = 80)
    public String lastName;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "USER_ID")
    public User user;

    @OneToMany(mappedBy = "owner")
    public List<Trip> myOwnTrips;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "TRAVELLER_TRIP",
            joinColumns = @JoinColumn(name = "TRIP_ID"),
            inverseJoinColumns = @JoinColumn(name = "TRAVELLER_ID"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"TRIP_ID", "TRAVELLER_ID"}))
    public List<Trip> trips;
}
