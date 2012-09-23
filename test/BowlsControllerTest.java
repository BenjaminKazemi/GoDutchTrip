import com.google.common.collect.ImmutableMap;
import helper.GenericFunctionalTest;
import models.Bowl;
import models.Expense;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Router;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

public class BowlsControllerTest extends GenericFunctionalTest {

    @Before
    public void before() {
        Expense.deleteAll();
        Bowl.deleteAll();
    }

    @Test
    public void create() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        Http.Response response = post("/bowls", bowl);

        assertEquals( 200, (Object) response.status);

        Bowl returnedBowl = Bowl.fromJson( getContent( response ) );

        assertNotNull( returnedBowl.id );
        assertEquals(bowl.title, returnedBowl.title);
        assertEquals(bowl.description, returnedBowl.description);
        assertEquals(bowl.date.toString(), returnedBowl.date.toString());
    }

    @Test
    public void read() throws UnsupportedEncodingException, IllegalAccessException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        Http.Response response = post("/bowls", bowl);

        Bowl returnedBowl = Bowl.fromJson( getContent(response) );

        returnedBowl = Bowl.fromJson( getContent( get(Router.reverse("BowlsController.read", ImmutableMap.of("id", (Object) returnedBowl.id)).url) ) );

        assertNotNull( returnedBowl );
        assertEquals( bowl.title, returnedBowl.title );
        assertEquals( bowl.description, returnedBowl.description );
        assertEquals(bowl.date.toString(), returnedBowl.date.toString());
    }

    @Test
    public void update() throws UnsupportedEncodingException, IllegalAccessException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        Bowl returnedBowl = Bowl.fromJson(getContent(post("/bowls", bowl)));
        bowl.title += " UPDATED";
        bowl.description += " UPDATED";
        Date d = bowl.date = new Date();

        returnedBowl = Bowl.fromJson( getContent( put(Router.reverse( "BowlsController.update", ImmutableMap.of("id", (Object) returnedBowl.id ) ).url, bowl) ) );
        assertNotNull( returnedBowl );
        assertNotNull( returnedBowl.id );
        assertEquals( bowl.title, returnedBowl.title );
        assertEquals( bowl.description, returnedBowl.description );
        assertEquals( bowl.date.toString(), returnedBowl.date.toString() );

        returnedBowl = Bowl.fromJson( getContent( get(Router.reverse("BowlsController.read", ImmutableMap.of("id", (Object) returnedBowl.id)).url) ) );
        assertNotNull( returnedBowl );
        assertEquals( bowl.title, returnedBowl.title );
        assertEquals( bowl.description, returnedBowl.description );
        assertEquals( bowl.date.toString(), returnedBowl.date.toString() );
    }

    @Test
    public void delete() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post( "/bowls", bowl ) ) );

        Http.Response response = delete(Router.reverse("BowlsController.delete", ImmutableMap.of("id", (Object) bowl.id)).url);
        assertEquals(200, (Object)response.status );

        response = GET(Router.reverse("BowlsController.read", ImmutableMap.of("id", (Object) bowl.id)).url);
        assertEquals( Http.StatusCode.NOT_FOUND, (Object)response.status );
    }

    @Test
    public void list() throws IllegalAccessException, UnsupportedEncodingException {
        List<Bowl> bowls = Bowl.listFromJson( getContent( get( Router.reverse( "BowlsController.list" ).url ) ) );
        assertEquals( 0, bowls.size() );

        for( int i = 0 ; i< 10; i++ ) {
            Bowl.fromJson( getContent( post( "/bowls", new Bowl( "chicago Trip " + i, "description " + i, new Date() ) ) ) );
        }

        bowls = Bowl.listFromJson(getContent(get(Router.reverse("BowlsController.list").url)));
        assertEquals( 10, bowls.size() );
    }

    @Test
    public void addParticipant() throws IllegalAccessException, UnsupportedEncodingException {
    }

    @Test
    public void addExpense() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post("/bowls", bowl) ) );
        Expense expense = new Expense( "Description", 215.0F, new Date() );

        bowl = Bowl.fromJson( getContent( put(Router.reverse("BowlsController.addExpense", ImmutableMap.of("id", (Object) bowl.id)).url, expense) ) );
        assertEquals( 1, bowl.expenses.size() );
        assertEquals( expense.description, bowl.expenses.get(0).description );
        assertEquals( expense.cost, bowl.expenses.get(0).cost);
        assertEquals(expense.date.toString(), bowl.expenses.get(0).date.toString());

        bowl = Bowl.fromJson( getContent( get(Router.reverse("BowlsController.read", ImmutableMap.of("id", (Object) bowl.id)).url) ) );
        assertEquals( 1, bowl.expenses.size() );
        assertEquals( expense.description, bowl.expenses.get(0).description );
        assertEquals( expense.cost, bowl.expenses.get(0).cost);
        assertEquals( expense.date.toString(), bowl.expenses.get(0).date.toString() );

        bowl = Bowl.fromJson( getContent( put(Router.reverse("BowlsController.addExpense", ImmutableMap.of("id", (Object) bowl.id)).url, expense) ) );
        assertEquals( 2, bowl.expenses.size() );

        assertEquals( expense.description, bowl.expenses.get(0).description );
        assertEquals( expense.cost, bowl.expenses.get(0).cost);
        assertEquals( expense.date.toString(), bowl.expenses.get(0).date.toString() );

        assertEquals( expense.description, bowl.expenses.get(1).description );
        assertEquals( expense.cost, bowl.expenses.get(1).cost);
        assertEquals( expense.date.toString(), bowl.expenses.get(1).date.toString() );

        bowl = Bowl.fromJson( getContent( get(Router.reverse("BowlsController.read", ImmutableMap.of("id", (Object) bowl.id)).url) ) );
        assertEquals( 2, bowl.expenses.size() );

        assertEquals( expense.description, bowl.expenses.get(0).description );
        assertEquals( expense.cost, bowl.expenses.get(0).cost);
        assertEquals( expense.date.toString(), bowl.expenses.get(0).date.toString() );

        assertEquals( expense.description, bowl.expenses.get(1).description );
        assertEquals( expense.cost, bowl.expenses.get(1).cost);
        assertEquals( expense.date.toString(), bowl.expenses.get(1).date.toString() );

    }

}