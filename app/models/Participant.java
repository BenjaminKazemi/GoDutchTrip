package models;

import util.annotation.IgnoreGson;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 10/20/12
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "tbl_participant")
public class Participant extends GenericModel {
    @ManyToOne
    public User user;

    @ManyToOne
    @IgnoreGson
    public Expense expense;

    public Float quota;

    public Participant() {}

    public Participant(User user, Float quota) {
        this.user = user;
        this.quota = quota;
    }

    public Participant( User user, Expense expense ) {
        this.user = user;
        this.expense = expense;
    }

    public static List<Participant> calculateShares(Bowl bowl) {
        List<Participant> retValue = new ArrayList<Participant>();

        Participant p = null;
        for( User user : bowl.users ) {
            Object[] obj = find(" " +
                    " SELECT u, (SELECT SUM(p.quota) FROM Participant p JOIN p.expense e JOIN e.bowl b WHERE p.user = u AND b = ?) " +
                    " FROM User u " +
                    " WHERE u.id = ? ",
                    bowl, user.id).first();
            try {
                Float quota = 0F;
                if( obj[1] != null ) {
                    quota = Float.valueOf(obj[1].toString());
                }
                p = new Participant( (User)obj[0], quota );
            }
            catch( Exception ignore ) {}
            if( p != null ) {
                retValue.add( p );
            }
        }

        return retValue;
    }
}
