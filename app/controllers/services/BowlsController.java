package controllers.services;

import models.Bowl;
import models.User;
import util.controller.GenericController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 9/22/12
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */

public class BowlsController extends GenericController {

    public static void create( Bowl bowl, List<Long> users ) {
        bowl.owner = currentUser;
        bowl.save();
        bowl.addUser( currentUser );

        if( users != null && !users.isEmpty() ) {
            for( Long id : users ) {
                User u = User.findById(id);
                if( u != null ) {
                    bowl.addUser( u );
                }
            }
        }

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

    public static void addUser(Long id, Long pId) {
        Bowl bowl = Bowl.findById( id );
        User user = User.findById( pId );

        bowl.addUser(user);

        renderJSON( bowl );
    }

    public static void deleteUser(Long id, Long pId) {
        Bowl bowl = Bowl.findById( id );
        User user = User.findById( pId );

        bowl.removeUser(user);

        renderJSON( bowl );
    }

}
