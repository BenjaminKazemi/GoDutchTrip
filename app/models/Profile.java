package models;

import models.security.User;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.List;

@Table(name = "PROFILE")
@Entity
public class Profile extends Model {
    @Column(name = "OWNER_ID")
    public User owner;
    @Column(name = "NAME", nullable = false, length = 50)
    public String name;
    @ManyToMany()
    @JoinTable(name = "PROFILE_TRIP", joinColumns = {@JoinColumn(name = "PROFILE_ID")}, inverseJoinColumns = {@JoinColumn(name = "TRIP_ID")})
    public List<Trip> trips;
}
