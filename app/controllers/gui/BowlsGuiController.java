package controllers.gui;

import models.Bowl;
import util.controller.GuiController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 10/8/12
 * Time: 8:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class BowlsGuiController extends GuiController {

    public static void create() {
        render();
    }

    public static void list() {
        List<Bowl> bowls = Bowl.findAll();

        render( bowls );
    }

    public static void show( Long id ) {
        Bowl bowl = Bowl.findById( id );

        render( bowl );
    }

    public static void delete( Long id ) {
        Bowl bowl = Bowl.findById( id );
        bowl.delete();

        render( bowl );
    }

}
