package models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import play.db.jpa.Model;
import util.annotation.IgnoreGSon;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 9/22/12
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "tbl_participant")
public class Participant extends GenericModel {
    public String fullName;
    public String email;
    public String username;
    @IgnoreGSon
    public String password;

    public Participant() {}

    public Participant(String username, String password, String email, String fullName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
    }

    public static Participant fromJson(String json) {
        return new Gson().fromJson(json, Participant.class);
    }

    public static List<Participant> listFromJson(String json) {
        return new Gson().fromJson( json, new TypeToken<List<Participant>>() {}.getType() );
    }
}
