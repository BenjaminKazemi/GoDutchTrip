package controllers;

import play.mvc.With;
import util.controller.GenericController;

@With(Secure.class)
@Check("admin")
public class Travellers extends GenericController {
}
