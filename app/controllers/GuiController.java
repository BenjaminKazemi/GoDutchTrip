package controllers;

import models.SecurityModel;
import models.User;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;
import util.security.ISecurityManager;
import util.security.ServiceSecurityManager;

public class GuiController extends Controller {
    protected static User currentUser = null;
    protected static String apiKey = "";

    static ISecurityManager securityManager = new ServiceSecurityManager();

    @Before
    public static void checkAuth() {
        if( request.url.startsWith( Router.reverse("GuiController.signIn").url )
            || request.url.startsWith( Router.reverse("Application.signUp").url ) ) {
            return;
        }
        apiKey = session.get( "api_key" );
//        apiKey = params.get( "api_key" );
        currentUser = securityManager.getUser( apiKey );
        if ( currentUser != null ) {
            Logger.info( currentUser + "[" + request.domain + "] " + request.url );
            renderArgs.put("currentUser", currentUser);
            renderArgs.put( "api_key", apiKey );
            return;
        }

        Logger.debug( "An unauthorized user tried to access " + request.url );
        signIn( request.url, null, null );
    }

    public static void signIn( String url, String username, String password ) {
        if( url == null || url.isEmpty() ) {
            url = Router.reverse("Application.app").url;
        }

        if( username != null && !username.isEmpty() && password != null && !password.isEmpty() ) {
            SecurityModel securityModel = securityManager.signIn( username, password );
            if( securityModel != null ) {
                currentUser = securityModel.user;
                session.put( "api_key", securityModel.securityKey );
                redirect( url );
            }
        }

        render( url );
    }

    public static void signOut() {
        securityManager.signOut( apiKey );
        signIn( Router.reverse("Application.app").url, null, null );
    }

}
