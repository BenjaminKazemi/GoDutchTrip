package controllers;

import controllers.gui.BowlsGuiController;
import models.User;
import play.libs.WS;
import play.mvc.Router;

import java.io.UnsupportedEncodingException;

public class Application extends GuiController {

    public static void index() {
        renderHtml("ok");
    }

    public static void app() {
        if( currentUser.isAdmin() ) {
            render();
        }
        BowlsGuiController.list();
    }

}