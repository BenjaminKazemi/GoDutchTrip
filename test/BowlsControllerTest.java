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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BowlsControllerTest extends GenericFunctionalTest {

    @Before
    public void before() {
        Participant.deleteAll();
        Expense.deleteAll();
        deleteAllBowlParticipants();
        Bowl.deleteAll();
    }

    public static void deleteAllBowlParticipants() {
        List<Bowl> bowls = Bowl.findAll();
        for( Bowl bowl:bowls ) {
            bowl.users.clear();
            bowl.save();
        }
    }

    @Test
    public void create() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        Http.Response response = post(Router.reverse(BowlsController_create).url, "bowl", bowl);

        assertEquals( 200, (Object) response.status);

        Bowl returnedBowl = Bowl.fromJson( getContent( response ) );

        assertNotNull( returnedBowl.id );
        assertEquals(bowl.title, returnedBowl.title);
        assertEquals(bowl.description, returnedBowl.description);
        assertEquals(bowl.date.toString(), returnedBowl.date.toString());
    }

    @Test
    public void createWithParticipants() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );

        List<Long> users = new ArrayList<Long>(2);
        users.add(User.fromJson(getContent(post(Router.reverse(UsersController_create).url, "participant", new User("Username1", "Password1", "EMail1", "Full Name1")))).id);
        users.add(User.fromJson(getContent(post(Router.reverse(UsersController_create).url, "participant", new User("Username2", "Password2", "EMail2", "Full Name2")))).id);
        // Extra user for test purposes
        post(Router.reverse(UsersController_create).url, "participant", new User("Username3", "Password3", "EMail3", "Full Name3"));

        Http.Response response = post(Router.reverse(BowlsController_create).url, "bowl", bowl, "users", users );

        assertEquals(200, (Object) response.status);

        Bowl returnedBowl = Bowl.fromJson( getContent( response ) );

        assertNotNull( returnedBowl.id );
        assertEquals( bowl.title, returnedBowl.title );
        assertEquals( bowl.description, returnedBowl.description );
        assertEquals( bowl.date.toString(), returnedBowl.date.toString() );
        assertEquals( users.size(), returnedBowl.users.size() );
    }

    @Test
    public void read() throws UnsupportedEncodingException, IllegalAccessException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        Http.Response response = post(Router.reverse(BowlsController_create).url, "bowl", bowl);

        Bowl returnedBowl = Bowl.fromJson(getContent(response));

        returnedBowl = Bowl.fromJson( getContent( get(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) returnedBowl.id)).url) ) );

        assertNotNull( returnedBowl );
        assertEquals( bowl.title, returnedBowl.title );
        assertEquals( bowl.description, returnedBowl.description );
        assertEquals(bowl.date.toString(), returnedBowl.date.toString());
    }

    @Test
    public void update() throws UnsupportedEncodingException, IllegalAccessException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        Bowl returnedBowl = Bowl.fromJson(getContent(post(Router.reverse(BowlsController_create).url, "bowl", bowl)));
        bowl.title += " UPDATED";
        bowl.description += " UPDATED";
        bowl.date = new Date();

        returnedBowl = Bowl.fromJson( getContent( put(Router.reverse( BowlsController_update, ImmutableMap.of("id", (Object) returnedBowl.id ) ).url, bowl) ) );
        assertNotNull( returnedBowl );
        assertNotNull( returnedBowl.id );
        assertEquals( bowl.title, returnedBowl.title );
        assertEquals( bowl.description, returnedBowl.description );
        assertEquals( bowl.date.toString(), returnedBowl.date.toString() );

        returnedBowl = Bowl.fromJson( getContent( get(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) returnedBowl.id)).url) ) );
        assertNotNull( returnedBowl );
        assertEquals( bowl.title, returnedBowl.title );
        assertEquals( bowl.description, returnedBowl.description );
        assertEquals( bowl.date.toString(), returnedBowl.date.toString() );
    }

    @Test
    public void delete() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post( Router.reverse(BowlsController_create).url, "bowl", bowl ) ) );

        Http.Response response = delete(Router.reverse(BowlsController_delete, ImmutableMap.of("id", (Object) bowl.id)).url);
        assertEquals(200, (Object)response.status );

        response = GET(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) bowl.id)).url);
        assertEquals( Http.StatusCode.NOT_FOUND, (Object)response.status );
    }

    @Test
    public void list() throws IllegalAccessException, UnsupportedEncodingException {
        List<Bowl> bowls = Bowl.listFromJson( getContent( get( Router.reverse( BowlsController_list ).url ) ) );
        assertEquals( 0, bowls.size() );

        for( int i = 0 ; i< 10; i++ ) {
            Bowl.fromJson( getContent( post( Router.reverse(BowlsController_create).url, "bowl", new Bowl( "chicago Trip " + i, "description " + i, new Date() ) ) ) );
        }

        bowls = Bowl.listFromJson(getContent(get(Router.reverse(BowlsController_list).url)));
        assertEquals( 10, bowls.size() );
    }

    @Test
    public void addParticipant() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post(Router.reverse(BowlsController_create).url, "bowl", bowl) ) );

        User user = new User( "Username", "Password", "EMail", "Full Name" );
        user = User.fromJson(getContent(post(Router.reverse(UsersController_create).url, "user", user)));

        bowl = Bowl.fromJson( getContent( put(Router.reverse(BowlsController_addUser, ImmutableMap.of("id", (Object) bowl.id, "pId", user.id)).url) ) );
        assertEquals( 1, bowl.users.size() );
        assertEquals( bowl.users.get(0).email, user.email );
        assertEquals( bowl.users.get(0).fullName, user.fullName );
        assertEquals( bowl.users.get(0).username, user.username );

        user = new User( "Username", "Password", "EMail", "Full Name" );
        user = User.fromJson(getContent(post(Router.reverse(UsersController_create).url, "user", user)));

        bowl = Bowl.fromJson( getContent( put(Router.reverse(BowlsController_addUser, ImmutableMap.of("id", (Object) bowl.id, "pId", user.id)).url) ) );
        assertEquals( 2, bowl.users.size() );

        assertEquals( bowl.users.get(0).email, user.email );
        assertEquals( bowl.users.get(0).fullName, user.fullName );
        assertEquals( bowl.users.get(0).username, user.username );

        assertEquals( bowl.users.get(1).email, user.email );
        assertEquals( bowl.users.get(1).fullName, user.fullName );
        assertEquals( bowl.users.get(1).username, user.username );

    }

    @Test
    public void deleteParticipant() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        User user = new User( "Username", "Password", "EMail", "Full Name" );

        bowl = Bowl.fromJson( getContent( post(Router.reverse(BowlsController_create).url, "bowl", bowl) ) );

        user = User.fromJson(getContent(post(Router.reverse(UsersController_create).url, "user", user)));
        bowl = Bowl.fromJson( getContent( put(Router.reverse(BowlsController_addUser, ImmutableMap.of("id", (Object) bowl.id, "pId", user.id)).url) ) );
        assertEquals( 1, bowl.users.size() );

        user = User.fromJson(getContent(post(Router.reverse(UsersController_create).url, "user", user)));
        bowl = Bowl.fromJson( getContent( put(Router.reverse(BowlsController_addUser, ImmutableMap.of("id", (Object) bowl.id, "pId", user.id)).url) ) );
        assertEquals( 2, bowl.users.size() );

        Http.Response response = delete(Router.reverse(BowlsController_deleteUser, ImmutableMap.of("id", (Object) bowl.id, "pId", bowl.users.get(0).id)).url);
        assertEquals(200, (Object)response.status );

        bowl = Bowl.fromJson( getContent( get(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) bowl.id)).url) ) );
        assertEquals( 1, bowl.users.size() );

        response = delete(Router.reverse(BowlsController_deleteUser, ImmutableMap.of("id", (Object) bowl.id, "pId", bowl.users.get(0).id)).url);
        assertEquals(200, (Object)response.status );

        bowl = Bowl.fromJson( getContent( get(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) bowl.id)).url) ) );
        assertEquals( 0, bowl.users.size() );
    }

}