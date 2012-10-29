package util.security;

import models.SecurityModel;
import models.User;
import play.Logger;
import play.mvc.Scope;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 10/22/12
 * Time: 6:45 PM
 * To change this template use File | Settings | File Templates.
 */

public class ServiceSecurityManager implements ISecurityManager {

    public User getUser( String key ) {
        return SecurityModel.findUserBySecurityKey( key );
    }

    public SecurityModel getSecurityModel( String key ) {
        return SecurityModel.findBySecurityKey( key );
    }

    public SecurityModel signIn( String username, String password ) {
        User user = User.findUser( username );
        if( user != null ) {
            if( user.password.equals( password ) ) {
                return SecurityModel.create( user );
            }
        }

        return null;
    }

    public boolean signOut( String key ) {
        return SecurityModel.invalidate( key );
    }

}
