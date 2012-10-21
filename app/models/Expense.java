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

    @OneToMany( cascade = CascadeType.ALL, mappedBy = "expense", fetch = FetchType.EAGER )
    public List<Participant> participants = new ArrayList<Participant>();

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
        Participant.delete(" expense = ? AND user = ?", this, user);
        recalculateShares();
    }

}
