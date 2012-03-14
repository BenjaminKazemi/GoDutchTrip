package controllers;

import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
@Check("guest")
public class Application extends Controller {

    public static void index() {
        render();
    }

}