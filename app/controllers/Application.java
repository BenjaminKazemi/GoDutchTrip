package controllers;

import util.controller.GenericController;

public class Application extends GenericController {

    public static void index() {
        renderHtml("ok");
    }

}