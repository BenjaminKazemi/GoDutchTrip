package controllers.services;

import models.Participant;
import util.controller.GenericController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 9/22/12
 * Time: 11:21 PM
 * To change this template use File | Settings | File Templates.
 */

public class ParticipantsController extends GenericController {

    public static void create( Participant participant) {
        participant.save();

        renderJSON( participant );
    }

    public static void read( Long id ) {
        Participant participant = Participant.findById( id );

        if( participant == null ) {
            notFound();
        }

        renderJSON( participant );
    }

    public static void update( Long id, Participant participant ) {
        Participant p = Participant.findById( id );

        if( participant == null ) {
            notFound();
        }

        if( participant.email != null ) {
            p.email = participant.email;
        }
        if( participant.fullName != null ) {
            p.fullName = participant.fullName;
        }
        if( participant.password != null ) {
            p.password = participant.password;
        }
        if( participant.username != null ) {
            p.username = participant.username;
        }
        p.save();

        renderJSON( p );
    }

    public static void delete( Long id ) {
        Participant participant = Participant.findById( id );

        if( participant == null ) {
            notFound();
        }

        participant.delete();

        ok();
    }

    public static void list() {
        List<Participant> participants = Participant.findAll();

        renderJSON( participants );
    }

}
