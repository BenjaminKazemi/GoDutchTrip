package controllers.gui;

import controllers.GuiController;
import models.*;
import util.Pagination;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 10/8/12
 * Time: 8:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class BowlsGuiController extends GuiController {

    public static void create() {
        render();
    }

    public static void edit( Long id ) {
        Bowl bowl = Bowl.findById( id );

        render( bowl );
    }

    public static void list() {
        List<Bowl> bowls = Bowl.findByUser( currentUser );

        render( bowls );
    }

    public static void show( Long id ) {
        Bowl bowl = Bowl.findById(id);

        List<Participant> bowlParticipants = Participant.calculateShares( bowl );
        List<Bill> bills = Bill.generateByBowl( bowl );

        render( bowl, bowlParticipants, bills );
    }

    public static void delete( Long id ) {
        Bowl bowl = Bowl.findById( id );
        bowl.delete();

        render( bowl );
    }

    public static void users( Long id, String query ) {
        Bowl bowl = Bowl.findById( id );
        List<User> users = null;
        if( query != null && !query.isEmpty() ) {
            users = User.findByUsernameExcludeBowls(query, bowl, new Pagination(1, 10));
        }

        render( bowl, users, query );
    }

    public static void expenses( Long id ) {
        Bowl bowl = Bowl.fetchUsersById( id );
        List<User> users = bowl.users;

        render( bowl, users );
    }

    public static void expense( Long id ) {
        Expense expense = Expense.findById( id );
        List<Bill> bills = Bill.generateByExpense( expense );

        render( expense, bills );
    }

    public static void expenseParticipants( Long id, String query ) {
        Expense expense = Expense.findById( id );
        List<User> users = null;
        if( query != null && !query.isEmpty() ) {
            users = User.findByUsernameExcludeExpense(query, expense, new Pagination(1, 10));
        }

        render( expense, users, query );
    }

}
