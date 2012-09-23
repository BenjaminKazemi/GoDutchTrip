package util.controller;

import models.GenericModel;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;

public class GenericController extends Controller {
    protected static Logger logger;

    @Before(priority = 1)
    public static void injectRequiredData() {
//        renderArgs.put("currentUser", currentUser);
    }

    protected static void renderJSON(GenericModel obj) {
        renderJSON( obj.toJson() );
    }
}
