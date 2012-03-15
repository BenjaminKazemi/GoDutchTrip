package controllers;

import models.Profile;
import play.mvc.With;

@With(Secure.class)
@Check("admin")
@CRUD.For(Profile.class)
public class Profiles extends CRUD {
}
