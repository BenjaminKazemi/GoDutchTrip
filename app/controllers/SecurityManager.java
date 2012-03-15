package controllers;

import models.security.User;
import play.Logger;
import util.constant.RoleConstants;

public class SecurityManager extends Secure.Security {
    static boolean authenticate(String username, String password) {
        User user = User.find("byUsername", username).first();
        Logger.info("%s is trying to login.", username);
        return user != null && user.password.equals(password);
    }

    static boolean check(String profile) {
        User user = User.find("byUsername", connected()).first();
        if (RoleConstants.ADMIN.equals(profile)) {
            return user.isAdmin();
        } else if (RoleConstants.GUEST.equals(profile)) {
            return user.isGuest();
        } else {
            return user.isRole(profile);
        }
    }

    public static User getCurrentUser() {
        return User.find("byUsername", connected()).first();
    }
}
