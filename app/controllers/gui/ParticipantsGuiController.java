package controllers.gui;

import models.Bowl;
import models.Participant;
import util.controller.GuiController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 10/8/12
 * Time: 8:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParticipantsGuiController extends GuiController {

    public static void create() {
        render();
    }

    public static void list() {
        List<Participant> participants = Participant.findAll();

        render( participants );
    }

    public static void show( Long id ) {
        Participant participant = Participant.findById(id);

        render( participant );
    }

    public static void delete( Long id ) {
        Participant participant = Participant.findById( id );
        participant.delete();

        render( participant );
    }

}
