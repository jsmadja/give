package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.restricted;

@Security.Authenticated(Secured.class)
public class Restricted extends Controller {

    public static Result index() {
        return ok(restricted.render());
    }
}