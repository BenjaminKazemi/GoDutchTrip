package helper;

import models.GenericModel;
import org.junit.Test;
import play.mvc.Http;
import play.test.FunctionalTest;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 9/22/12
 * Time: 9:42 PM
 * To change this template use File | Settings | File Templates.
 */

public class GenericFunctionalTest extends FunctionalTest {

    @Test
    public void test() {}

    public static Http.Request newRequest() {
        Http.Request request = FunctionalTest.newRequest();
        request.contentType = "application/json";
        return request;
    }

    public Http.Response get(String url) {
        return GET( newRequest(), url );
    }

    public static Http.Response post( String url, GenericModel obj ) throws UnsupportedEncodingException, IllegalAccessException {
        return POST( newRequest(), url + "?" + obj.toUrlParams(obj.getClass().getSimpleName().toLowerCase()) );
    }

    public static Http.Response put( String url, GenericModel obj ) throws UnsupportedEncodingException, IllegalAccessException {
        return  PUT( newRequest(), url + "?" + obj.toUrlParams(obj.getClass().getSimpleName().toLowerCase()), "", "" );
    }

    public static Http.Response put( String url ) throws UnsupportedEncodingException, IllegalAccessException {
        return  PUT( newRequest(), url, "", "" );
    }

    public Http.Response delete(String url) {
        return DELETE( newRequest(), url );
    }

}
