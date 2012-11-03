package controllers.services;

import models.SecurityModel;
import models.User;
import play.mvc.Controller;
import util.security.ServiceSecurityManager;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 10/24/12
 * Time: 9:42 PM
 * To change this template use File | Settings | File Templates.
 */

public class LoginController extends Controller {

    public static void signIn( String email, String password ) {
        SecurityModel securityModel = new ServiceSecurityManager().signIn( email, password );
        if( securityModel != null ) {
            renderText(securityModel.securityKey);
        }

        unauthorized();
    }

    public static void signOut() {
        if( new ServiceSecurityManager().signOut( request.user ) ) {
            ok();
        }

        unauthorized();
    }

    public static void authenticated() {
        User user = new ServiceSecurityManager().getUser( request.user );
        if( user != null ) {
            renderJSON( user.toJson() );
        }

        unauthorized();
    }

}
