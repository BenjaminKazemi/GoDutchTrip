import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class ApplicationTest extends FunctionalTest {

    @Test
    public void index() {
        Response response = GET( "/" );
        assertIsOk( response );
        assertContentType( "text/html", response );
        assertContentMatch( "ok", response );
    }

    @Test
    public void signIn() {
        Response response = GET( "/signin" );
        assertIsOk( response );
    }

    @Test
    public void signUp() {
        Response response = GET( "/signup" );
        assertIsOk( response );
    }

}