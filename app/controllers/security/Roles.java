package controllers.security;

import controllers.CRUD;
import controllers.Check;
import controllers.Secure;
import models.security.Role;
import play.mvc.With;

@With(Secure.class)
@Check("admin")
@CRUD.For(Role.class)
public class Roles extends CRUD {
}
