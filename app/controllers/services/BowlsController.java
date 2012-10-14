package controllers.services;

import models.Bowl;
import models.Participant;
import util.controller.GenericController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 9/22/12
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */

public class BowlsController extends GenericController {

    public static void create( Bowl bowl, List<Long> participants ) {
        if( participants != null && !participants.isEmpty() ) {
            bowl.participants = new ArrayList<Participant>();
            for( Long id : participants ) {
                Participant p = Participant.findById( id );
                if( p != null ) {
                    bowl.participants.add( p );
                }
            }
        }
        bowl.save();

        renderJSON(bowl);
    }

    public static void read( Long id ) {
        Bowl bowl = Bowl.findById( id );

        if( bowl == null ) {
            notFound();
        }

        renderJSON(bowl);
    }

    public static void update( Long id, Bowl bowl ) {
        Bowl b = Bowl.findById( id );

        if( bowl == null ) {
            notFound();
        }

        if( bowl.title != null ) {
            b.title = bowl.title;
        }
        if( bowl.description != null ) {
            b.description = bowl.description;
        }
        if( bowl.date != null ) {
            b.date = bowl.date;
        }
        b.save();

        renderJSON(b);
    }

    public static void delete( Long id ) {
        Bowl bowl = Bowl.findById( id );

        if( bowl == null ) {
            notFound();
        }

        bowl.delete();

        ok();
    }

    public static void list() {
        List<Bowl> bowls = Bowl.findAll();

        renderJSON(bowls);
    }

    public static void addParticipant( Long id, Long pId ) {
        Bowl bowl = Bowl.findById(id);
        Participant participant = Participant.findById( pId );

        bowl.participants.add( participant );
        bowl.save();

        renderJSON(bowl);
    }

    public static void deleteParticipant( Long id, Long pId ) {
        Bowl bowl = Bowl.findById(id);
        Participant participant = Participant.findById( pId );

        bowl.participants.remove(participant);
        bowl.save();

        renderJSON(bowl);
    }

}
