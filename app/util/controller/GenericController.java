package util.controller;

import models.GenericModel;
import models.User;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;
import util.security.ISecurityManager;
import util.security.ServiceSecurityManager;

public class GenericController extends Controller {
    protected static User currentUser = null;
    protected static Logger logger;

    static ISecurityManager securityManager = new ServiceSecurityManager();

    @Before
    public static void checkAuth() {
        if( request.url.startsWith( Router.reverse("Services.UsersController.create").url ) ) {
            return;
        }
        if( request.user != null && !request.user.isEmpty() ) {
            currentUser = securityManager.getUser( request.user );
        } else {
            currentUser = securityManager.getUser( params.get("api_key") );
        }
        if( currentUser != null ){
            Logger.info(currentUser + "[" + request.domain + "] " + request.url);
            return;
        }

        Logger.debug( "An unauthorized user tried to access " + request.url );
        unauthorized();
    }

    @Before(priority = 1)
    public static void injectRequiredData() {
//        renderArgs.put("currentUser", currentUser);
    }

    protected static void renderJSON(GenericModel obj) {
        renderJSON( obj.toJson() );
    }
}
