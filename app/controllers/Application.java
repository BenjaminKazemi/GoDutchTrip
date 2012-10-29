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

    public static void signUpUser( User user ) {
        if( user != null ) {
            try {
                WS.HttpResponse response = WS.url("http://localhost:9000" + Router.reverse("services.UsersController.create").url).body(user.toParams("user")).post();
                if( response.success() ) {
                    user = User.fromJson( response.getString() );
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        render("/Application/signUp.html");
    }

    public static void signUp() {
        render();
    }

}