package controllers;

import models.security.User;
import play.Logger;
import util.constant.RoleConstants;
import util.security.Encryptor;

public class SecurityManager extends Secure.Security {
    private static User currentUser;

    static boolean authenticate(String username, String password) {
        currentUser = User.find("byUsername", username).first();
        Logger.info("%s is trying to login.", username);
        return currentUser != null && currentUser.password.equals(Encryptor.SHA_256.encrypt(password));
    }

    static boolean check(String profile) {
        if (currentUser == null) {
            currentUser = User.find("byUsername", connected()).first();
        }
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
        if (currentUser == null) {
            currentUser = User.find("byUsername", connected()).first();
        }
        return currentUser;
    }
}
