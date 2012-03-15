package controllers;

import models.Trip;
import play.mvc.With;

@With(Secure.class)
@Check("admin")
@CRUD.For(Trip.class)
public class Trips extends CRUD {
}
