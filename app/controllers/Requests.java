package controllers;

import models.Gift;
import models.GiveMail;
import models.Request;
import models.User;
import play.db.ebean.Model;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.requests;

import java.util.Map;

import static controllers.Users.currentUser;

@Security.Authenticated(Secured.class)
public class Requests extends Controller {

    public static Result index() {
        return ok(requests.render(currentUser()));
    }

    public static Result give() {
        Map<String, String[]> data = request().body().asFormUrlEncoded();
        Long requestId = Long.parseLong(data.get("requestId")[0]);
        Request request = Request.find.byId(requestId);
        Gift gift = Gift.find.byId(request.gift.id);
        User requester = User.find.byId(request.requester.id);
        gift.given = true;
        gift.update();
        flash(Application.FLASH_MESSAGE_KEY, "Vous souhaitez donner " + gift.name + " à " + requester.name + ". Un email a été envoyé à " + requester.email);

        new GiveMail(request).send();
        gift.requests.stream().forEach(Model::delete);
        return redirect(routes.Requests.index());
    }

}
