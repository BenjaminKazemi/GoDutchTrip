package controllers.security;

import controllers.CRUD;
import controllers.Check;
import controllers.Secure;
import models.security.User;
import play.mvc.With;

@With(Secure.class)
@Check("admin")
@CRUD.For(User.class)
public class Users extends CRUD {
}
