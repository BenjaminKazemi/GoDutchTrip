package controllers;

import models.SecurityModel;
import models.User;
import play.Logger;
import play.libs.WS;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;
import util.menu.MenuManager;
import util.security.ISecurityManager;
import util.security.ServiceSecurityManager;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class GuiController extends Controller {
    public static User currentUser = null;
    protected static String apiKey = "";

    static ISecurityManager securityManager = new ServiceSecurityManager();

    @Before
    public static void checkAuth() {
        if( request.url.startsWith( Router.reverse("GuiController.signIn").url )
            || request.url.startsWith( Router.reverse("GuiController.signUp").url )
            || request.url.startsWith( Router.reverse("GuiController.home").url ) ) {
                renderArgs.put( "menu", MenuManager.hardCode( User.GUEST ) );
            return;
        }
        apiKey = session.get( "api_key" );
//        apiKey = params.get( "api_key" );
        currentUser = securityManager.getUser( apiKey );

        renderArgs.put( "menu", MenuManager.hardCode( currentUser ) );

        if ( currentUser != null && !currentUser.isGuest()) {
            Logger.info( currentUser + "[" + request.domain + "] " + request.url );
            renderArgs.put("currentUser", currentUser);
            renderArgs.put( "api_key", apiKey );
            return;
        }

        Logger.debug( "An unauthorized user tried to access " + request.url );
        home();
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

        home();
    }

    public static void signOut() {
        securityManager.signOut(apiKey);
        home();
    }

    public static void home() {
        render();
    }

/*
    public static void signUpUser( User user ) {
        if( user != null ) {
            try {
                WS.HttpResponse response = WS.url("http://localhost:9000" + Router.reverse("services.UsersController.create").url).body(user.toParams("user")).post();
                if( response.success() ) {
                    user = User.fromJson( response.getString() );
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        render("/views/GuiController/signUp.html");
    }
*/

    public static void signUp() {
        render();
    }

}
