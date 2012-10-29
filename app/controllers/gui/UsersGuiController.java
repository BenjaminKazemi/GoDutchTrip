package controllers.gui;

import controllers.GuiController;
import models.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 10/8/12
 * Time: 8:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class UsersGuiController extends GuiController {

    public static void create() {
        render();
    }

    public static void list() {
        List<User> users = User.findAll();

        render(users);
    }

    public static void show( Long id ) {
        User user = User.findById(id);

        render(user);
    }

    public static void delete( Long id ) {
        User user = User.findById(id);
        user.delete();

        render(user);
    }

}
