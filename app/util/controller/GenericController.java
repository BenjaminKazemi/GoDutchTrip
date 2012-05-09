package util.controller;

import models.Trip;
import models.security.User;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;

import java.util.List;

public class GenericController extends Controller {
    protected static Logger logger;
    protected static User currentUser;
    protected static List<Trip> ownedTrips;
    protected static List<Trip> otherTrips;

    @Before
    public static void injectRequiredData() {
        currentUser = controllers.SecurityManager.getCurrentUser();
//        ownedTrips = currentUser.traveller.myOwnTrips;
//        otherTrips = Trip.find("SELECT t FROM Trip t JOIN t.travellers v JOIN v.user u WHERE u = ?1", currentUser).fetch();
        ownedTrips = currentUser.traveller.myOwnTrips;
        otherTrips = currentUser.traveller.trips;

        renderArgs.put("currentUser", currentUser);
        renderArgs.put("ownedTrips", ownedTrips);
        renderArgs.put("otherTrips", otherTrips);
    }
}
