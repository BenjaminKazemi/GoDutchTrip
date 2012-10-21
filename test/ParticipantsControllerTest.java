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
import java.util.List;

public class ParticipantsControllerTest extends GenericFunctionalTest {

    @Before
    public void before() {
        Participant.deleteAll();
        Expense.deleteAll();
        deleteAllBowlUsers();
        Bowl.deleteAll();
        User.deleteAll();
    }

    public static void deleteAllBowlUsers() {
        List<Bowl> bowls = Bowl.findAll();
        for( Bowl bowl:bowls ) {
            bowl.users.clear();
            bowl.save();
        }
    }

    @Test
    public void create() throws IllegalAccessException, UnsupportedEncodingException {
        User user = new User( "Username", "Password", "EMail", "Full Name" );
        Http.Response response = post(Router.reverse(UsersController_create).url, "user", user);

        assertEquals( 200, (Object) response.status);

        User returnedUser = User.fromJson(getContent(response));

        assertNotNull(returnedUser.id);
        assertEquals(user.username, returnedUser.username);
        assertEquals(user.email, returnedUser.email);
        assertEquals(user.fullName, returnedUser.fullName);
    }

    @Test
    public void read() throws UnsupportedEncodingException, IllegalAccessException {
        User user = new User( "Username", "Password", "EMail", "Full Name" );
        Http.Response response = post(Router.reverse(UsersController_create).url, "user", user);

        User returnedUser = User.fromJson(getContent(response));

        returnedUser = User.fromJson(getContent(get(Router.reverse(UsersController_read, ImmutableMap.of("id", (Object) returnedUser.id)).url)));

        assertNotNull(returnedUser);
        assertEquals(user.username, returnedUser.username);
        assertEquals(user.email, returnedUser.email);
        assertEquals(user.fullName, returnedUser.fullName);
    }

    @Test
    public void update() throws UnsupportedEncodingException, IllegalAccessException {
        User user = new User( "Username", "Password", "EMail", "Full Name" );
        User returnedBowl = User.fromJson(getContent(post(Router.reverse(UsersController_create).url, "user", user)));
        user.username += " UPDATED";
        user.email += " UPDATED";
        user.fullName += " UPDATED";

        returnedBowl = User.fromJson(getContent(put(Router.reverse(UsersController_update, ImmutableMap.of("id", (Object) returnedBowl.id)).url, user)));
        assertNotNull( returnedBowl );
        assertNotNull( returnedBowl.id );
        assertEquals( user.username, returnedBowl.username );
        assertEquals( user.email, returnedBowl.email );
        assertEquals( user.fullName, returnedBowl.fullName );

        returnedBowl = User.fromJson(getContent(get(Router.reverse(UsersController_read, ImmutableMap.of("id", (Object) returnedBowl.id)).url)));
        assertNotNull( returnedBowl );
        assertEquals( user.username, returnedBowl.username );
        assertEquals( user.email, returnedBowl.email );
        assertEquals( user.fullName, returnedBowl.fullName );
    }

    @Test
    public void delete() throws IllegalAccessException, UnsupportedEncodingException {
        User user = new User( "Username", "Password", "EMail", "Full Name" );
        user = User.fromJson(getContent(post(Router.reverse(UsersController_create).url, "user", user)));

        Http.Response response = delete(Router.reverse(UsersController_delete, ImmutableMap.of("id", (Object) user.id)).url);
        assertEquals(200, (Object)response.status );

        response = GET(Router.reverse(UsersController_read, ImmutableMap.of("id", (Object) user.id)).url);
        assertEquals( Http.StatusCode.NOT_FOUND, (Object)response.status );
    }

    @Test
    public void list() throws IllegalAccessException, UnsupportedEncodingException {
        List<User> bowls = User.listFromJson(getContent(get(Router.reverse(UsersController_list).url)));
        assertEquals( 0, bowls.size() );

        for( int i = 0 ; i< 10; i++ ) {
            User.fromJson(getContent(post(Router.reverse(UsersController_create).url, "participant", new User("Username" + i, "Password" + i, "EMail" + i, "Full Name" + i))));
        }

        bowls = User.listFromJson(getContent(get(Router.reverse(UsersController_list).url)));
        assertEquals( 10, bowls.size() );
    }

}