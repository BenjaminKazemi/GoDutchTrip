package models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import play.data.binding.As;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_bowl")
public class Bowl extends GenericModel {
    public String title;
    public String description;
    @As({"MM/dd/yyyy HH:mm:ss", "MM/dd/yyyy"})
    public Date date;
    public Float cost = 0F;

    @ManyToMany
    @JoinTable( name = "tbl_bowl_user", joinColumns = {@JoinColumn(name = "bowl_id")} )
    @OrderBy("username")
    public List<User> users = new ArrayList<User>();

    @OneToMany( mappedBy = "bowl", cascade = CascadeType.ALL )
    @OrderBy("cost DESC, date ASC")
    public List<Expense> expenses;

    public Bowl() {
    }

    public Bowl(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public static Bowl fromJson(String json) {
        return new Gson().fromJson(json, Bowl.class);
    }

    public static List<Bowl> listFromJson(String json) {
        return new Gson().fromJson( json, new TypeToken<List<Bowl>>() {}.getType() );
    }

    public void recalculateShares() {
        for( Expense expense : expenses ) {
            for( Participant p : expense.participants ) {
                p.quota = expense.cost / expense.participants.size();
                p.save();
            }
        }
    }

    public void addUser( User user ) {
        users.add( user );
        save();
    }

    public void removeUser( User user ) {
        users.remove( user );
        save();
        for( Expense expense : expenses ) {
            expense.removeParticipant( user );
        }
        recalculateShares();
    }

    public void addExpense( Expense expense ) {
        expenses.add( expense );
        cost += expense.cost;
        save();
    }

    public static Bowl fetchUsersAndExpensesById(Long id) {
        return find( " FROM Bowl b JOIN FETCH b.users WHERE b.id = ? ", id ).first();
    }

}