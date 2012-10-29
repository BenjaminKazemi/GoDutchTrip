package util.security;

import models.SecurityModel;
import models.User;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 10/24/12
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */

public interface ISecurityManager {
    User getUser( String key );
    SecurityModel signIn( String username, String password );
    boolean signOut( String key );
}