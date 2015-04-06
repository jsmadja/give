package controllers;

import com.feth.play.module.pa.PlayAuthenticate;
import models.Category;
import models.User;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.users;
import views.html.users_read;

public class Users extends Controller {

    public static Result index() {
        return ok(users.render(currentUser(), User.getContacts()));
    }

    public static Result read(User user) {
        return ok(users_read.render(user));
    }

    public static User getLocalUser(final Http.Session session) {
        return User.findByAuthUserIdentity(PlayAuthenticate.getUser(session));
    }

    public static User currentUser() {
        return getLocalUser(session());
    }

}
