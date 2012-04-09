package controllers;

import models.Trip;
import models.security.User;
import play.mvc.With;
import util.controller.GenericController;

import java.util.List;

@With(Secure.class)
@Check("guest")
public class Application extends GenericController {

    public static void index() {
        render();
    }

}