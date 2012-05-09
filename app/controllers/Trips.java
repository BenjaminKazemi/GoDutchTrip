package controllers;

import models.Trip;
import play.data.validation.Validation;
import play.mvc.With;
import util.controller.GenericController;

@With(Secure.class)
@Check("admin")
public class Trips extends GenericController {

    public static void list() {
        render();
    }

    public static void create(Trip trip) {
        boolean success = false;
        boolean create = true;
        if (trip != null) {
            trip.owner = currentUser.traveller;
            if (validation.valid(trip).ok) {
                trip.save();
                ownedTrips.add(trip);
                trip = new Trip();
                success = true;
            }
        }
        render(create, trip, success);
    }

    public static void update(Long tripId, Trip trip) {
        if (tripId == null || tripId < 1) {
            Validation.addError("error", "No trip was selected.");
        } else if (trip == null) {
            Validation.addError("error", "Trip info was not sent.");
        } else {
            Trip originalTrip = Trip.findById(tripId);

            if (originalTrip != null && currentUser.traveller.equals(originalTrip.owner)) {
                originalTrip.name = trip.name;
                originalTrip.origin = trip.origin;
                originalTrip.destination = trip.destination;
                originalTrip.departDate = trip.departDate;
                originalTrip.returnDate = trip.returnDate;
                originalTrip.isRound = trip.isRound;

                if (validation.valid(originalTrip).ok) {
                    originalTrip.save();
                    edit(tripId);
                } else {
                    render("edit.html", trip);
                }
            }
        }
//        render("/Trips/edit.html", trip);
        Application.index();
    }

    public static void show(Long tripId) {
        Trip trip = Trip.findById(tripId);
        render(trip);
    }

    public static void edit(Long tripId) {
        Trip trip = Trip.findById(tripId);
        render(trip);
    }

    public static void blank() {
        boolean create = true;
        Trip trip = new Trip();
        render("/trips/create.html", create, trip);
    }

    public static void delete(Long tripId) {
        if (tripId != null) {
            Trip trip = Trip.findById(tripId);
            if (trip != null) {
                trip.delete();
            }
        }
        Application.index();
    }
}
