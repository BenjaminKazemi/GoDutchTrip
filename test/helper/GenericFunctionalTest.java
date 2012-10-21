package helper;

import models.GenericModel;
import org.junit.Test;
import play.mvc.Http;
import play.test.FunctionalTest;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
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

    public static Http.Request newRequest() {
        Http.Request request = FunctionalTest.newRequest();
        request.contentType = "application/json";
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
