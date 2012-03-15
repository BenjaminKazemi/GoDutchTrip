package controllers;

import models.security.User;
import play.mvc.With;
import util.controller.GenericController;

@With(Secure.class)
@Check("guest")
public class Application extends GenericController {

    public static void index() {
        User currentUser = getCurrentUser();
        render(currentUser);
    }

}