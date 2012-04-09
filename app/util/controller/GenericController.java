package util.controller;

import models.Trip;
import models.security.User;
import play.Logger;
import play.mvc.Controller;

import java.util.List;

public class GenericController extends Controller {
    protected static Logger logger;

    protected static User getCurrentUser() {
        return controllers.SecurityManager.getCurrentUser();
    }

    protected static void render(Object... args) {
        User currentUser = getCurrentUser();
        List<Trip> ownedTrips = Trip.find("byOwner", currentUser.traveller).fetch();
        List<Trip> otherTrips = Trip.find("SELECT t FROM Trip t JOIN t.travellers v JOIN v.user u WHERE u = ?1", currentUser).fetch();

        Controller.render(args, currentUser, ownedTrips, otherTrips);
    }
}
