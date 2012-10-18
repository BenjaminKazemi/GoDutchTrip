package controllers.gui;

import models.Bowl;
import models.Expense;
import models.Participant;
import util.Pagination;
import util.controller.GuiController;

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
        List<Bowl> bowls = Bowl.findAll();

        render( bowls );
    }

    public static void show( Long id ) {
        Bowl bowl = Bowl.findById(id);

        render( bowl );
    }

    public static void delete( Long id ) {
        Bowl bowl = Bowl.findById( id );
        bowl.delete();

        render( bowl );
    }

    public static void participants( Long id, String query ) {
        Bowl bowl = Bowl.findById( id );
//        List<Participant> participants = Participant.find( "username LIKE ? AND username NOT IN (SELECT p.username FROM Bowl b JOIN b.participants p WHERE b.id = ?)", query + "%", bowl.id ).fetch(1,10);
        List<Participant> participants = null;
        if( query != null && !query.isEmpty() ) {
//            participants = Participant.find( "username LIKE ?", query + "%" ).fetch( 1, 10 );
//            participants = Participant.find( "username LIKE ? AND username NOT IN (SELECT p.username FROM Bowl b JOIN b.participants p WHERE b.id = ?)", query + "%", bowl.id ).fetch(1,10);
            participants = Participant.findUsersByUsernameExcludeBowls( query, bowl, new Pagination(1,10) );
        }

        render( bowl, participants, query );
    }

    public static void expenses( Long id ) {
        Bowl bowl = Bowl.findById( id );
        render( bowl );
    }

    public static void expense( Long id ) {
        Expense expense = Expense.findById( id );
        render( expense );
    }

}
