package controllers;

import models.Trip;
import models.security.User;
import play.mvc.With;
import util.controller.GenericController;

import java.util.List;

@With(Secure.class)
@Check("admin")
public class Trips extends GenericController {

    public static void list() {
        User currentUser = getCurrentUser();
        List<Trip> ownedTrips = Trip.find("byOwner", currentUser.traveller).fetch();
        List<Trip> otherTrips = Trip.find("SELECT t FROM Trip t JOIN t.travellers v JOIN v.user u WHERE u = ?1", currentUser).fetch();
        GenericController.render(currentUser, ownedTrips, otherTrips);
    }

    public static void create() {
        GenericController.render();
    }
}
