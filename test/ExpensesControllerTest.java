import com.google.common.collect.ImmutableMap;
import helper.GenericFunctionalTest;
import models.Bowl;
import models.Expense;
import models.Participant;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Router;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

public class ExpensesControllerTest extends GenericFunctionalTest {

    @Before
    public void before() {
        deleteAllExpensesParticipants();
        Expense.deleteAll();
        deleteAllBowlParticipants();
        Bowl.deleteAll();
    }

    public static void deleteAllBowlParticipants() {
        List<Bowl> bowls = Bowl.findAll();
        for( Bowl bowl:bowls ) {
            bowl.participants.clear();
            bowl.save();
        }
    }

    public static void deleteAllExpensesParticipants() {
        List<Expense> expenses = Expense.findAll();
        for( Expense expense:expenses ) {
            expense.participants.clear();
            expense.save();
        }
    }

    @Test
    public void update() throws UnsupportedEncodingException, IllegalAccessException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post("/bowls", bowl) ) );
        Expense expense = new Expense( "Description", 215.0F, new Date() );

        bowl = Bowl.fromJson( getContent( post(Router.reverse("BowlsController.addExpense", ImmutableMap.of("id", (Object) bowl.id)).url, expense) ) );
        bowl.expenses.get(0).cost += 10;
        bowl.expenses.get(0).description += " UPDATED";
        bowl.expenses.get(0).date = new Date();

        Expense returnedExpense = Expense.fromJson( getContent( put(Router.reverse( "ExpensesController.update", ImmutableMap.of("id", (Object) bowl.expenses.get(0).id ) ).url, bowl.expenses.get(0)) ) );
        assertNotNull( returnedExpense );
        assertNotNull( returnedExpense.id );
        assertEquals( bowl.expenses.get(0).cost, returnedExpense.cost );
        assertEquals( bowl.expenses.get(0).description, returnedExpense.description );
        assertEquals( bowl.expenses.get(0).date.toString(), returnedExpense.date.toString() );
    }

    @Test
    public void delete() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post("/bowls", bowl) ) );
        Expense expense = new Expense( "Description", 215.0F, new Date() );

        bowl = Bowl.fromJson( getContent( post(Router.reverse("BowlsController.addExpense", ImmutableMap.of("id", (Object) bowl.id)).url, expense) ) );
        bowl = Bowl.fromJson( getContent( post(Router.reverse("BowlsController.addExpense", ImmutableMap.of("id", (Object) bowl.id)).url, expense) ) );

        bowl = Bowl.fromJson( getContent( get(Router.reverse("BowlsController.read", ImmutableMap.of("id", (Object) bowl.id)).url) ) );
        assertEquals( 2, bowl.expenses.size() );

        Http.Response response = delete(Router.reverse("ExpensesController.delete", ImmutableMap.of("id", (Object) bowl.expenses.get(0).id)).url);
        assertEquals(200, (Object)response.status );

        bowl = Bowl.fromJson( getContent( get(Router.reverse("BowlsController.read", ImmutableMap.of("id", (Object) bowl.id)).url) ) );
        assertEquals( 1, bowl.expenses.size() );

        response = delete(Router.reverse("ExpensesController.delete", ImmutableMap.of("id", (Object) bowl.expenses.get(0).id)).url);
        assertEquals(200, (Object)response.status );

        bowl = Bowl.fromJson( getContent( get(Router.reverse("BowlsController.read", ImmutableMap.of("id", (Object) bowl.id)).url) ) );
        assertEquals( 0, bowl.expenses.size() );
    }

    @Test
    public void addParticipant() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post("/bowls", bowl) ) );
        Expense expense = new Expense( "Description", 215.0F, new Date() );

        expense = Bowl.fromJson( getContent( post(Router.reverse("BowlsController.addExpense", ImmutableMap.of("id", (Object) bowl.id)).url, expense) ) ).expenses.get(0);

        Participant participant = new Participant( "Username", "Password", "EMail", "Full Name" );
        participant = Participant.fromJson( getContent( post("/participants", participant) ) );

        expense = Expense.fromJson( getContent( put(Router.reverse("ExpensesController.addParticipant", ImmutableMap.of("id", (Object) expense.id, "pId", participant.id)).url) ) );
        assertEquals( 1, expense.participants.size() );
        assertEquals( expense.participants.get(0).email, participant.email );
        assertEquals( expense.participants.get(0).fullName, participant.fullName );
        assertEquals( expense.participants.get(0).username, participant.username);

        participant = new Participant( "Username", "Password", "EMail", "Full Name" );
        participant = Participant.fromJson( getContent( post("/participants", participant) ) );

        expense = Expense.fromJson( getContent( put(Router.reverse("ExpensesController.addParticipant", ImmutableMap.of("id", (Object) expense.id, "pId", participant.id)).url) ) );
        assertEquals(2, expense.participants.size());

        assertEquals( expense.participants.get(0).email, participant.email );
        assertEquals( expense.participants.get(0).fullName, participant.fullName );
        assertEquals( expense.participants.get(0).username, participant.username );

        assertEquals( expense.participants.get(1).email, participant.email );
        assertEquals( expense.participants.get(1).fullName, participant.fullName );
        assertEquals( expense.participants.get(1).username, participant.username );

    }

    @Test
    public void deleteParticipant() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post("/bowls", bowl) ) );
        Expense expense = new Expense( "Description", 215.0F, new Date() );

        expense = Bowl.fromJson( getContent( post(Router.reverse("BowlsController.addExpense", ImmutableMap.of("id", (Object) bowl.id)).url, expense) ) ).expenses.get(0);

        Participant participant = new Participant( "Username", "Password", "EMail", "Full Name" );
        participant = Participant.fromJson( getContent( post("/participants", participant) ) );

        expense = Expense.fromJson( getContent( put(Router.reverse("ExpensesController.addParticipant", ImmutableMap.of("id", (Object) expense.id, "pId", participant.id)).url) ) );
        assertEquals( 1, expense.participants.size() );

        participant = new Participant( "Username", "Password", "EMail", "Full Name" );
        participant = Participant.fromJson( getContent( post("/participants", participant) ) );

        expense = Expense.fromJson( getContent( put(Router.reverse("ExpensesController.addParticipant", ImmutableMap.of("id", (Object) expense.id, "pId", participant.id)).url) ) );
        assertEquals( 2, expense.participants.size() );

        Http.Response response = delete(Router.reverse("ExpensesController.deleteParticipant", ImmutableMap.of("id", (Object) expense.id, "pId", expense.participants.get(0).id)).url);
        assertEquals(200, (Object)response.status );

        expense = Bowl.fromJson( getContent( get(Router.reverse("BowlsController.read", ImmutableMap.of("id", (Object) bowl.id)).url) ) ).expenses.get(0);
        assertEquals(1, expense.participants.size());

        response = delete(Router.reverse("ExpensesController.deleteParticipant", ImmutableMap.of("id", (Object) expense.id, "pId", expense.participants.get(0).id)).url);
        assertEquals(200, (Object)response.status );

        expense = Bowl.fromJson( getContent( get(Router.reverse("BowlsController.read", ImmutableMap.of("id", (Object) bowl.id)).url) ) ).expenses.get(0);
        assertEquals(0, expense.participants.size());
    }

}