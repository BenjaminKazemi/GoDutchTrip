package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "TRIP")
@Entity
public class Trip extends Model {
    @Column(name = "NAME", nullable = false, length = 150)
    public String name;

    @Column(name = "ORIGIN", length = 150)
    public String origin;

    @Column(name = "DESTINATION", length = 150)
    public String destination;

    @Column(name = "DEPART_DATE", length = 150)
    public Date departDate;

    @Column(name = "RETURN_DATE", length = 150)
    public Date returnDate;

    @Column(name = "IS_ROUND")
    public boolean isRound;

    @ManyToOne(optional = false)
    @JoinColumn(name = "OWNER_ID")
    public Traveller owner;

    @ManyToMany(mappedBy = "trips")
    public List<Traveller> travellers;

}
