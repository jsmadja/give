package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.myrequests;

import static controllers.Users.currentUser;
import static models.Request.findAllRequestsFor;

@Security.Authenticated(Secured.class)
public class MyRequests extends Controller {

    public static Result index() {
        return ok(myrequests.render(currentUser()));
    }

}
