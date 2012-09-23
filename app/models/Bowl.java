package models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import play.data.binding.As;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_bowl")
public class Bowl extends GenericModel {
    public String title;
    public String description;
    @As("MM/dd/yyyy HH:mm:ss")
    public Date date;
    public Float cost;

    @ManyToMany
    @JoinTable(name = "tbl_bowl_participant")
    public List<Participant> participants;

    @OneToMany(mappedBy = "bowl" )
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

}