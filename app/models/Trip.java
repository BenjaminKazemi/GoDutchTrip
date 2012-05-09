package models;

import play.data.binding.As;
import play.data.validation.Check;
import play.data.validation.CheckWith;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "TRIP")
@Entity
public class Trip extends Model {
    @Column(name = "NAME", nullable = false, length = 150)
    @Required(message = "Name must not be empty.")
    @MaxSize(150)
    public String name;

    @Column(name = "ORIGIN", length = 150)
    @MaxSize(150)
    public String origin;

    @Column(name = "DESTINATION", length = 150)
    @MaxSize(150)
    public String destination;

    @Column(name = "DEPART_DATE")
    @As(value="dd/MM/yyyy")
    public Date departDate;

    @Column(name = "RETURN_DATE")
    @As(value="dd/MM/yyyy")
    @CheckWith(value = CheckDates.class, message = "The return date must be after the departure date.")
    public Date returnDate;

    @Column(name = "IS_ROUND")
    public boolean isRound;

    @ManyToOne(optional = false)
    @JoinColumn(name = "OWNER_ID")
    public Traveller owner;

    @ManyToMany(mappedBy = "trips")
    public List<Traveller> travellers;

    public class CheckDates extends Check {
        @Override
        public boolean isSatisfied(Object validatedObject, Object returnDate) {
            if (returnDate != null && ((Trip) validatedObject).departDate != null) {
                return !((Trip) validatedObject).departDate.after((Date) returnDate);
            }
            return true;
        }
    }
}
