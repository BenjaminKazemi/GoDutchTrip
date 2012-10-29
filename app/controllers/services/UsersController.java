package controllers.services;

import models.User;
import models.enums.Role;
import util.controller.GenericController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 9/22/12
 * Time: 11:21 PM
 * To change this template use File | Settings | File Templates.
 */

public class UsersController extends GenericController {

    public static void create( User user ) {
        user.role = Role.USER;
        user.save();

        renderJSON(user);
    }

    public static void read( Long id ) {
        User user = User.findById(id);

        if( user == null ) {
            notFound();
        }

        renderJSON(user);
    }

    public static void update( Long id, User user ) {
        User p = User.findById(id);

        if( user == null ) {
            notFound();
        }

        if( user.email != null ) {
            p.email = user.email;
        }
        if( user.fullName != null ) {
            p.fullName = user.fullName;
        }
        if( user.password != null ) {
            p.password = user.password;
        }
        if( user.username != null ) {
            p.username = user.username;
        }
        p.save();

        renderJSON( p );
    }

    public static void delete( Long id ) {
        User user = User.findById(id);

        if( user == null ) {
            notFound();
        }

        user.delete();

        ok();
    }

    public static void list() {
        List<User> users = User.findAll();

        renderJSON(users);
    }

}
