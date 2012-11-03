import helper.GenericFunctionalTest;
import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class ApplicationTest extends GenericFunctionalTest {

    @Test
    public void index() {
        Response response = get("/");
        assertIsOk( response );
    }

    @Test
    public void signIn() {
        Response response = get( "/signin" );
        assertIsOk( response );
    }

    @Test
    public void signUp() {
        Response response = get( "/signup" );
        assertIsOk( response );
    }

}