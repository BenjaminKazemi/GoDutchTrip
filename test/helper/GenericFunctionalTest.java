package helper;

import models.*;
import models.enums.Role;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Router;
import play.test.FunctionalTest;
import util.security.ISecurityManager;
import util.security.ServiceSecurityManager;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 9/22/12
 * Time: 9:42 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class GenericFunctionalTest extends FunctionalTest {
    private static final String CONTENT_TYPE_URL = "application/x-www-form-urlencoded";

    public static final String LoginController_signIn = "services.LoginController.signIn";
    public static final String LoginController_signOut = "services.LoginController.signOut";
    public static final String LoginController_authenticated = "services.LoginController.authenticated";

    public static final String BowlsController_create = "services.BowlsController.create";
    public static final String BowlsController_list = "services.BowlsController.list";
    public static final String BowlsController_read = "services.BowlsController.read";
    public static final String BowlsController_update = "services.BowlsController.update";
    public static final String BowlsController_delete = "services.BowlsController.delete";
    public static final String BowlsController_addUser = "services.BowlsController.addUser";
    public static final String BowlsController_deleteUser = "services.BowlsController.deleteUser";

    public static final String UsersController_create = "services.UsersController.create";
    public static final String UsersController_list = "services.UsersController.list";
    public static final String UsersController_read = "services.UsersController.read";
    public static final String UsersController_update = "services.UsersController.update";
    public static final String UsersController_delete = "services.UsersController.delete";

    public static final String ExpensesController_create = "services.ExpensesController.create";
    public static final String ExpensesController_update = "services.ExpensesController.update";
    public static final String ExpensesController_delete = "services.ExpensesController.delete";
    public static final String ExpensesController_addParticipant = "services.ExpensesController.addParticipant";
    public static final String ExpensesController_addAllParticipants = "services.ExpensesController.addAllParticipants";
    public static final String ExpensesController_deleteParticipant = "services.ExpensesController.deleteParticipant";

    protected static String key;

    @Before
    public void before() throws UnsupportedEncodingException, IllegalAccessException {
        deleteAllParticipants();
        Participant.deleteAll();
        Expense.deleteAll();
        Bowl.deleteAll();
        SecurityModel.deleteAll();
        User.deleteAll();

        login();
    }

    public static void deleteAllParticipants() {
        List<Bowl> bowls = Bowl.findAll();
        for( Bowl bowl:bowls ) {
            List<User> users = new ArrayList<User>(bowl.users);
            for( User u : users) {
                bowl.removeUser( u );
            }
        }
        List<Expense> expenses = Expense.findAll();
        for( Expense expense : expenses ) {
            List<Participant> participants = new ArrayList<Participant>(expense.participants);
            for( Participant p : participants ) {
                expense.removeParticipant( p.user );
            }
        }
    }

    protected User login() throws IllegalAccessException, UnsupportedEncodingException {
        double rand = Math.random();
        User user = new User();
        user.username = "test_user_" + rand;
        user.password = "test_password_" + rand;
        user.role = Role.USER;
        user.fullName = "Test User " + rand;
        user.save();

        key = getContent( post( Router.reverse(LoginController_signIn).url, "username", user.username, "password", user.password ) );

        return user;
    }

    protected void logout() throws IllegalAccessException, UnsupportedEncodingException {
        get( Router.reverse(LoginController_signOut).url );
    }

    public GenericFunctionalTest() {
//        SecurityModel.deleteAll();
//        try { User.deleteAll(); }
//        catch( Exception ignore ) {}
    }

    public static Http.Request newRequest() {
        Http.Request request = FunctionalTest.newRequest();
        request.contentType = "application/json";
        if( key != null && !key.isEmpty() ) {
            request.user = key;
        }
        return request;
    }

    public Http.Response get(String url) {
        return GET( newRequest(), url );
    }

    public static Http.Response post( String url, Object...objects ) throws UnsupportedEncodingException, IllegalAccessException {
//        return POST( newRequest(), url + "?" + obj.toParams(obj.getClass().getSimpleName().toLowerCase()) );
        String body = "";
        for( int i=0; i < objects.length; ) {
            String name = objects[i++].toString();
            Object obj = objects[i++];
            if( obj instanceof GenericModel ) {
                body += ((GenericModel)obj).toParams( obj.getClass().getSimpleName().toLowerCase() );
            } else if( obj instanceof List ) {
                List list = (List)obj;
                int j = 0;
                for( Object item : list ) {
                    body += name + "[" + j++ + "]=" + URLEncoder.encode( item.toString(), Charset.defaultCharset().toString() );
                    if( j < list.size() ) {
                        body += "&";
                    }
                }
            } else if( obj instanceof String ) {
                body += name + "=" + URLEncoder.encode( obj.toString(), Charset.defaultCharset().toString() );
            }

            if( i < objects.length ) {
                body += "&";
            }
        }
        return POST( newRequest(), url, CONTENT_TYPE_URL, body );
    }

    public static Http.Response put( String url, GenericModel obj ) throws UnsupportedEncodingException, IllegalAccessException {
//        return  PUT( newRequest(), url + "?" + obj.toParams(obj.getClass().getSimpleName().toLowerCase()), "", "" );
        return  PUT( newRequest(), url, CONTENT_TYPE_URL, obj.toParams(obj.getClass().getSimpleName().toLowerCase()) );
    }

    public static Http.Response put( String url ) throws UnsupportedEncodingException, IllegalAccessException {
        return  PUT( newRequest(), url, "", "" );
    }

    public Http.Response delete(String url) {
        return DELETE( newRequest(), url );
    }

}
