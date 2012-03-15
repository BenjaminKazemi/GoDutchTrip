package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.List;

@Table(name = "TRIP")
@Entity
public class Trip extends Model {
    @Column(name = "TITLE", nullable = false, length = 150)
    public String title;
    @Column(name = "ORIGIN", length = 150)
    public String origin;
    @Column(name = "DESTINATION", length = 150)
    public String destination;
    @ManyToMany(mappedBy = "trips")
    public List<Profile> fellowTravelers;
}
