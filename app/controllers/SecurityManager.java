package controllers;

import models.security.User;
import play.Logger;
import util.constant.RoleConstants;
import util.security.Encryptor;

public class SecurityManager extends Secure.Security {
//    private static User currentUser;

    static boolean authenticate(String username, String password) {
//        User currentUser = getCurrentUser();
        User currentUser = User.find("byUsername", username).first();
        Logger.info("%s is trying to login.", username);
        return currentUser != null && currentUser.password.equals(Encryptor.SHA_256.encrypt(password));
    }

    static boolean check(String profile) {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            if (RoleConstants.ADMIN.equals(profile)) {
                return currentUser.isAdmin();
            } else if (RoleConstants.GUEST.equals(profile)) {
                return currentUser.isGuest();
            } else {
                return currentUser.isRole(profile);
            }
        }
        return false;
    }

    public static User getCurrentUser() {
//        if (currentUser == null) {
//            currentUser = User.find("byUsername", connected()).first();
//            currentUser = User.find("SELECT u FROM User u FETCH JOIN u.traveller t FETCH t.myOwnTrips FETCH JOIN t.trips WHERE u = ?1", connected()).first();
//        }
//        return currentUser;
        return User.fetchUserByUsername(connected());
    }
}
