package models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import util.Pagination;
import util.annotation.IgnoreGSon;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 9/22/12
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "tbl_user")
public class User extends GenericModel {
    public String fullName;
    public String email;
    public String username;
    @IgnoreGSon
    public String password;

    public User() {}

    public User(String username, String password, String email, String fullName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
    }

    public static User fromJson(String json) {
        return new Gson().fromJson(json, User.class);
    }

    public static List<User> listFromJson(String json) {
        return new Gson().fromJson( json, new TypeToken<List<User>>() {}.getType() );
    }

    public static List<User> findUser(String username) {
        return User.findAll();
    }

    public static List<User> findByUsernameExcludeBowls( String usernameStarts, Bowl bowl, Pagination pagination ) {
        List<User> users = new ArrayList<User>();

        JPAQuery query = User.find(" lower(username) LIKE ? " +
                " AND id NOT IN (SELECT u.id FROM Bowl b JOIN b.users u WHERE b.id = ?)",
                usernameStarts.toLowerCase() + "%",
                bowl.id);

        if( pagination != null ) {
            users = query.fetch( pagination.page, pagination.length );
        } else {
            users = query.fetch();
        }

        return users;
    }

    public static List<User> findByUsernameExcludeExpense( String usernameStarts, Expense expense, Pagination pagination ) {
        List<User> users = new ArrayList<User>();
        JPAQuery query = User.find(" lower(username) LIKE ? " +
                " AND id IN (SELECT u1.id FROM Bowl b JOIN b.users u1 WHERE b = ?) " +
                " AND id NOT IN (SELECT p.user.id FROM Expense e JOIN e.participants p WHERE e.id = ?) ",
                usernameStarts.toLowerCase() + "%",
                expense.bowl,
                expense.id);

        if( pagination != null ) {
            users = query.fetch( pagination.page, pagination.length );
        } else {
            users = query.fetch();
        }

        return users;
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

}
