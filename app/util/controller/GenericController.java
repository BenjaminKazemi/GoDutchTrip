package util.controller;

import models.security.User;
import play.Logger;
import play.mvc.Controller;

public class GenericController extends Controller {
    protected static Logger logger;

    protected static User getCurrentUser() {
        return controllers.SecurityManager.getCurrentUser();
    }
}
