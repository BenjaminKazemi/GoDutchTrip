import helper.GenericFunctionalTest;
import models.SecurityModel;
import models.User;
import models.enums.Role;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.db.jpa.GenericModel;
import play.mvc.Http;
import play.mvc.Router;
import util.security.ServiceSecurityManager;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class LoginControllerTest extends GenericFunctionalTest {

    @Before
    public void setUp() {
        SecurityModel.deleteAll();
        User.deleteAll();
    }

    @After
    public void clear() {
        SecurityModel.deleteAll();
        User.deleteAll();
    }

    @Test
    public void signIn() throws IllegalAccessException, UnsupportedEncodingException {
        User user = createUser();

        String key = getContent( post( Router.reverse(LoginController_signIn).url, "email", user.email, "password", user.password ) );
        User retUser = new ServiceSecurityManager().getUser( key );

        assertNotNull( retUser );
        assertEquals( user.id, retUser.id );
        assertEquals( user.nickName, retUser.nickName );
        assertEquals( user.password, retUser.password );
        assertEquals( user.role, retUser.role );
    }

    @Test
    public void signOut() throws IllegalAccessException, UnsupportedEncodingException {
        login();

        User retUser = new ServiceSecurityManager().getUser( key );
        assertNotNull( retUser );

        Http.Response response = get( Router.reverse(LoginController_signOut).url );
        assertEquals( 200, response.status.intValue() );

        retUser = new ServiceSecurityManager().getUser( key );
        assertEquals( User.GUEST, retUser );
    }

    @Test
    public void authenticated() throws UnsupportedEncodingException, IllegalAccessException {
        login();

        Http.Response response = get( Router.reverse(LoginController_authenticated).url );
        assertEquals( 200, response.status.intValue() );

        logout();

        User retUser = new ServiceSecurityManager().getUser( key );
        assertEquals( User.GUEST, retUser );
    }

}