package models;

import com.google.gson.Gson;
import play.data.binding.As;
import util.annotation.IgnoreGSon;

import javax.persistence.*;
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

    @ManyToMany
    @JoinTable(name = "tbl_expense_payer")
    public List<Participant> payer;

    @ManyToMany
    @JoinTable(name = "tbl_expense_participant")
    public List<Participant> participants;

    @ManyToOne
    @JoinColumn(name = "tbl_bowl_id")
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

}
