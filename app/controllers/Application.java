package controllers;

import models.InvitationMail;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.about;
import views.html.index;

import static controllers.Users.getLocalUser;

public class Application extends Controller {

    public static final String FLASH_MESSAGE_KEY = "message";
    public static final String FLASH_ERROR_KEY = "error";

    public static Result mail() {
        new InvitationMail("julien.smadja@gmail.com").send();
        return ok();
    }

    public static Result index() {
        return ok(index.render(getLocalUser(session())));
    }

    public static Result about() {
        return ok(about.render(getLocalUser(session())));
    }

    public static Result oAuthDenied(final String providerKey) {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        flash(FLASH_ERROR_KEY, "You need to accept the OAuth connection in order to use this website!");
        return redirect(routes.Application.index());
    }

}
