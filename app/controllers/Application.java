package controllers;

import com.feth.play.module.pa.PlayAuthenticate;
import models.User;
import play.mvc.Controller;
import play.mvc.Http.Session;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static final String FLASH_MESSAGE_KEY = "message";
    public static final String FLASH_ERROR_KEY = "error";

    public static Result index() {
        final User localUser = getLocalUser(session());
        return ok(index.render(localUser));
    }

    public static Result oAuthDenied(final String providerKey) {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        flash(FLASH_ERROR_KEY, "You need to accept the OAuth connection in order to use this website!");
        return redirect(routes.Application.index());
    }

    public static User getLocalUser(final Session session) {
        return User.findByAuthUserIdentity(PlayAuthenticate.getUser(session));
    }
}
