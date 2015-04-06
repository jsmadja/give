package controllers;

import models.Gift;
import models.Request;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.catalog;

import java.util.Map;

import static controllers.Users.currentUser;

@Security.Authenticated(Secured.class)
public class Catalog extends Controller {

    public static Result index() {
        return ok(catalog.render(currentUser()));
    }

}
