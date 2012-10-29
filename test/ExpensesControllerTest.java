import com.google.common.collect.ImmutableMap;
import helper.GenericFunctionalTest;
import models.Bowl;
import models.Expense;
import models.Participant;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Router;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

public class ExpensesControllerTest extends GenericFunctionalTest {

    @Test
    public void create() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post(Router.reverse(BowlsController_create).url, "bowl", bowl) ) );
        Expense expense = new Expense( "Description", 215.0F, new Date() );

        bowl = Bowl.fromJson( getContent( post(Router.reverse(ExpensesController_create, ImmutableMap.of("id", (Object) bowl.id)).url, "expenses", expense) ) );
        assertEquals( 1, bowl.expenses.size() );
        assertEquals( expense.description, bowl.expenses.get(0).description );
        assertEquals( expense.cost, bowl.expenses.get(0).cost);
        assertEquals(expense.date.toString(), bowl.expenses.get(0).date.toString());

        bowl = Bowl.fromJson( getContent( get(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) bowl.id)).url) ) );
        assertEquals( 1, bowl.expenses.size() );
        assertEquals( expense.description, bowl.expenses.get(0).description );
        assertEquals( expense.cost, bowl.expenses.get(0).cost);
        assertEquals( expense.date.toString(), bowl.expenses.get(0).date.toString() );

        bowl = Bowl.fromJson( getContent( post(Router.reverse(ExpensesController_create, ImmutableMap.of("id", (Object) bowl.id)).url, "expense", expense) ) );
        assertEquals( 2, bowl.expenses.size() );

        assertEquals( expense.description, bowl.expenses.get(0).description );
        assertEquals( expense.cost, bowl.expenses.get(0).cost);
        assertEquals( expense.date.toString(), bowl.expenses.get(0).date.toString() );

        assertEquals( expense.description, bowl.expenses.get(1).description );
        assertEquals( expense.cost, bowl.expenses.get(1).cost);
        assertEquals( expense.date.toString(), bowl.expenses.get(1).date.toString() );

        bowl = Bowl.fromJson( getContent( get(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) bowl.id)).url) ) );
        assertEquals( 2, bowl.expenses.size() );

        assertEquals( expense.description, bowl.expenses.get(0).description );
        assertEquals( expense.cost, bowl.expenses.get(0).cost);
        assertEquals( expense.date.toString(), bowl.expenses.get(0).date.toString() );

        assertEquals( expense.description, bowl.expenses.get(1).description );
        assertEquals( expense.cost, bowl.expenses.get(1).cost);
        assertEquals( expense.date.toString(), bowl.expenses.get(1).date.toString() );

    }

    @Test
    public void createWithPayer() throws Exception {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post(Router.reverse(BowlsController_create).url, "bowl", bowl) ) );

        User user = new User( "Username", "Password", "EMail", "Full Name" );
        user = User.fromJson(getContent(post(Router.reverse(UsersController_create).url, "user", user)));
        bowl = Bowl.fromJson( getContent( put(Router.reverse(BowlsController_addUser, ImmutableMap.of("id", (Object) bowl.id, "pId", user.id)).url) ) );

        Expense expense = new Expense( "Description", 215.0F, new Date() );
        expense.payer = user;
        bowl = Bowl.fromJson( getContent( post(Router.reverse(ExpensesController_create, ImmutableMap.of("id", (Object) bowl.id)).url, "expenses", expense) ) );

        assertEquals( expense.payer.id, bowl.expenses.get(0).payer.id );
        assertEquals( expense.payer.email, bowl.expenses.get(0).payer.email );
        assertEquals( expense.payer.fullName, bowl.expenses.get(0).payer.fullName );
        assertEquals( expense.payer.username, bowl.expenses.get(0).payer.username );

        assertEquals( 1, bowl.expenses.get(0).participants.size() );
        assertEquals( expense.payer.id, bowl.expenses.get(0).participants.get(0).user.id );
        assertEquals( expense.payer.email, bowl.expenses.get(0).participants.get(0).user.email );
        assertEquals( expense.payer.fullName, bowl.expenses.get(0).participants.get(0).user.fullName );
        assertEquals( expense.payer.username, bowl.expenses.get(0).participants.get(0).user.username );
    }

    @Test
    public void update() throws UnsupportedEncodingException, IllegalAccessException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post(Router.reverse(BowlsController_create).url, "bowl", bowl) ) );
        Expense expense = new Expense( "Description", 215.0F, new Date() );

        bowl = Bowl.fromJson( getContent( post(Router.reverse(ExpensesController_create, ImmutableMap.of("id", (Object) bowl.id)).url, "expenses", expense) ) );
        bowl.expenses.get(0).cost += 10;
        bowl.expenses.get(0).description += " UPDATED";
        bowl.expenses.get(0).date = new Date();

        Expense returnedExpense = Expense.fromJson( getContent( put(Router.reverse( ExpensesController_update, ImmutableMap.of("id", (Object) bowl.expenses.get(0).id ) ).url, bowl.expenses.get(0)) ) );
        assertNotNull( returnedExpense );
        assertNotNull( returnedExpense.id );
        assertEquals( bowl.expenses.get(0).cost, returnedExpense.cost );
        assertEquals( bowl.expenses.get(0).description, returnedExpense.description );
        assertEquals( bowl.expenses.get(0).date.toString(), returnedExpense.date.toString() );
    }

    @Test
    public void delete() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post(Router.reverse(BowlsController_create).url, "bowl", bowl) ) );
        Expense expense = new Expense( "Description", 215.0F, new Date() );

        bowl = Bowl.fromJson( getContent( post(Router.reverse(ExpensesController_create, ImmutableMap.of("id", (Object) bowl.id)).url, "expenses", expense) ) );
        bowl = Bowl.fromJson( getContent( post(Router.reverse(ExpensesController_create, ImmutableMap.of("id", (Object) bowl.id)).url, "expenses", expense) ) );

        bowl = Bowl.fromJson( getContent( get(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) bowl.id)).url) ) );
        assertEquals( 2, bowl.expenses.size() );

        Http.Response response = delete(Router.reverse(ExpensesController_delete, ImmutableMap.of("id", (Object) bowl.expenses.get(0).id)).url);
        assertEquals(200, (Object) response.status);

        bowl = Bowl.fromJson( getContent( get(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) bowl.id)).url) ) );
        assertEquals( 1, bowl.expenses.size() );

        response = delete(Router.reverse(ExpensesController_delete, ImmutableMap.of("id", (Object) bowl.expenses.get(0).id)).url);
        assertEquals(200, (Object)response.status );

        bowl = Bowl.fromJson( getContent( get(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) bowl.id)).url) ) );
        assertEquals( 0, bowl.expenses.size() );
    }

    @Test
    public void addParticipant() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post(Router.reverse(BowlsController_create).url, "bowl", bowl) ) );
        Expense expense = new Expense( "Description", 215.0F, new Date() );

        expense = Bowl.fromJson( getContent( post(Router.reverse(ExpensesController_create, ImmutableMap.of("id", (Object) bowl.id)).url, "expenses", expense) ) ).expenses.get(0);

        User user = new User( "Username", "Password", "EMail", "Full Name" );
        user = User.fromJson(getContent(post(Router.reverse(UsersController_create).url, "user", user)));

        expense = Expense.fromJson( getContent( put(Router.reverse(ExpensesController_addParticipant, ImmutableMap.of("id", (Object) expense.id, "pId", user.id)).url) ) );
        assertEquals( 1, expense.participants.size() );
        assertEquals( expense.participants.get(0).user.email, user.email );
        assertEquals( expense.participants.get(0).user.fullName, user.fullName );
        assertEquals( expense.participants.get(0).user.username, user.username);

        user = new User( "Username", "Password", "EMail", "Full Name" );
        user = User.fromJson(getContent(post(Router.reverse(UsersController_create).url, "user", user)));

        expense = Expense.fromJson( getContent( put(Router.reverse(ExpensesController_addParticipant, ImmutableMap.of("id", (Object) expense.id, "pId", user.id)).url) ) );
        assertEquals(2, expense.participants.size());

        assertEquals( expense.participants.get(0).user.email, user.email );
        assertEquals( expense.participants.get(0).user.fullName, user.fullName );
        assertEquals( expense.participants.get(0).user.username, user.username );

        assertEquals( expense.participants.get(1).user.email, user.email );
        assertEquals( expense.participants.get(1).user.fullName, user.fullName );
        assertEquals( expense.participants.get(1).user.username, user.username );

    }

    @Test
    public void deleteParticipant() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post(Router.reverse(BowlsController_create).url, "bowl", bowl) ) );
        Expense expense = new Expense( "Description", 215.0F, new Date() );

        expense = Bowl.fromJson( getContent( post(Router.reverse(ExpensesController_create, ImmutableMap.of("id", (Object) bowl.id)).url, "expense", expense) ) ).expenses.get(0);

        User user = new User( "Username", "Password", "EMail", "Full Name" );
        user = User.fromJson(getContent(post(Router.reverse(UsersController_create).url, "user", user)));

        expense = Expense.fromJson( getContent( put(Router.reverse(ExpensesController_addParticipant, ImmutableMap.of("id", (Object) expense.id, "pId", user.id)).url) ) );
        assertEquals( 1, expense.participants.size() );

        user = new User( "Username", "Password", "EMail", "Full Name" );
        user = User.fromJson(getContent(post(Router.reverse(UsersController_create).url, "user", user)));

        expense = Expense.fromJson( getContent( put(Router.reverse(ExpensesController_addParticipant, ImmutableMap.of("id", (Object) expense.id, "pId", user.id)).url) ) );
        assertEquals( 2, expense.participants.size() );

        Http.Response response = delete(Router.reverse(ExpensesController_deleteParticipant, ImmutableMap.of("id", (Object) expense.id, "pId", expense.participants.get(0).id)).url);
        assertEquals(200, (Object)response.status );

        expense = Bowl.fromJson( getContent( get(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) bowl.id)).url) ) ).expenses.get(0);
        assertEquals(1, expense.participants.size());

        response = delete(Router.reverse(ExpensesController_deleteParticipant, ImmutableMap.of("id", (Object) expense.id, "pId", expense.participants.get(0).id)).url);
        assertEquals(200, (Object)response.status );

        expense = Bowl.fromJson( getContent( get(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) bowl.id)).url) ) ).expenses.get(0);
        assertEquals(0, expense.participants.size());
    }

    @Test
    public void addAllParticipants() throws Exception {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post(Router.reverse(BowlsController_create).url, "bowl", bowl) ) );
        Expense expense = new Expense( "Description", 215.0F, new Date() );

        expense = Bowl.fromJson( getContent( post(Router.reverse(ExpensesController_create, ImmutableMap.of("id", (Object) bowl.id)).url, "expense", expense) ) ).expenses.get(0);

        User tmpUser = new User( "Username", "Password", "EMail", "Full Name" );
        post( Router.reverse(UsersController_create).url, "user", tmpUser );

        User user0 = new User( "Username0", "Password0", "EMail0", "Full Name 0" );
        user0 = User.fromJson(getContent( post( Router.reverse(UsersController_create).url, "user", user0 ) ) );
        put(Router.reverse(BowlsController_addUser, ImmutableMap.of("id", (Object) bowl.id, "pId", user0.id)).url );

        User user1 = new User( "Username1", "Password1", "EMail1", "Full Name 1" );
        user1 = User.fromJson(getContent( post( Router.reverse(UsersController_create).url, "user", user1 ) ) );
        put(Router.reverse(BowlsController_addUser, ImmutableMap.of("id", (Object) bowl.id, "pId", user1.id)).url);

        User user2 = new User( "Username2", "Password2", "EMail2", "Full Name 2" );
        user2 = User.fromJson(getContent( post( Router.reverse(UsersController_create).url, "user", user2 ) ) );
        put(Router.reverse(BowlsController_addUser, ImmutableMap.of("id", (Object) bowl.id, "pId", user2.id)).url);

        put(Router.reverse(ExpensesController_addParticipant, ImmutableMap.of("id", (Object) expense.id, "pId", user0.id)).url);
        expense = Expense.fromJson( getContent( put(Router.reverse(ExpensesController_addAllParticipants, ImmutableMap.of("id", (Object) expense.id)).url) ) );

        assertEquals(3, expense.participants.size());

        assertEquals( expense.participants.get(0).user.email, user0.email );
        assertEquals( expense.participants.get(0).user.fullName, user0.fullName );
        assertEquals( expense.participants.get(0).user.username, user0.username );

        assertEquals( expense.participants.get(1).user.email, user1.email );
        assertEquals( expense.participants.get(1).user.fullName, user1.fullName );
        assertEquals( expense.participants.get(1).user.username, user1.username );

        assertEquals( expense.participants.get(2).user.email, user2.email );
        assertEquals( expense.participants.get(2).user.fullName, user2.fullName );
        assertEquals( expense.participants.get(2).user.username, user2.username );

    }

}