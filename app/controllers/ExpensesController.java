package controllers;

import models.Bowl;
import models.Expense;
import models.Participant;
import util.controller.GenericController;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 9/22/12
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */

public class ExpensesController extends GenericController {

    public static void create( Long id, Expense expense ) {
        Bowl bowl = Bowl.findById( id );

        expense.bowl = bowl;
        expense.save();
        bowl.expenses.add(expense);
        bowl.save();

        renderJSON(bowl);
    }

    public static void delete( Long id ) {
        Expense expense = Expense.findById( id );

        if( expense == null ) {
            notFound();
        }

        expense.delete();

        ok();
    }

    public static void update( Long id, Expense expense ) {
        Expense e = Expense.findById( id );

        if( e == null ) {
            notFound();
        }

        if( expense.cost != null ) {
            e.cost = expense.cost;
        }
        if( expense.description != null ) {
            e.description = expense.description;
        }
        if( expense.date != null ) {
            e.date = expense.date;
        }
        e.save();

        renderJSON(e);
    }

    public static void addParticipant( Long id, Long pId ) {
        Expense expense = Expense.findById( id );
        Participant participant = Participant.findById( pId );

        expense.participants.add( participant );
        expense.save();

        renderJSON(expense);
    }

    public static void deleteParticipant( Long id, Long pId ) {
        Expense expense = Expense.findById( id );
        Participant participant = Participant.findById( pId );

        expense.participants.remove( participant );
        expense.save();

        renderJSON(expense);
    }

}
