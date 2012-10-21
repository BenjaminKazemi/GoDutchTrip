package models;

import com.google.gson.Gson;
import play.data.binding.As;
import util.Pagination;
import util.annotation.IgnoreGSon;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 9/22/12
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "tbl_expense")
public class Expense extends GenericModel {
    public String description;
    public Float cost;
    @As({"MM/dd/yyyy HH:mm:ss", "MM/dd/yyyy"})
    public Date date;

    @ManyToOne
    public User payer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expense")
    public List<Participant> participants;

    @ManyToOne
    @JoinColumn(name = "bowl_id")
    @IgnoreGSon
    public Bowl bowl;

    public Expense() {
    }

    public Expense(String description, Float cost, Date date) {
        this.description = description;
        this.cost = cost;
        this.date = date;
    }

    public static Expense fromJson(String json) {
        return new Gson().fromJson(json, Expense.class);
    }

    public static List<User> findAllNonParticipantUsers( Expense expense, Pagination pagination ) {
        List<User> users = new ArrayList<User>();
        JPAQuery query = User.find(" id IN (SELECT u.id FROM Expense e1 JOIN e1.bowl b JOIN b.users u WHERE e1 = ?) " +
                " AND id NOT IN (SELECT p.user.id FROM Expense e2 JOIN e2.participants p WHERE e2 = ? ) ",
                expense, expense );

        if( pagination != null ) {
            users = query.fetch( pagination.page, pagination.length );
        } else {
            users = query.fetch();
        }

        return users;
    }

    public void recalculateShares() {
        for( Participant p : participants ) {
            p.quota = cost / participants.size();
            p.save();
        }
    }

    public void addParticipant( Participant participant ) {
        participants.add( participant );
        save();
        recalculateShares();
    }

    public void removeParticipant( User user ) {
        Participant.delete(" expense = ? AND user = ?", this, user );
        recalculateShares();
    }

}
