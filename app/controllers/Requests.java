package controllers;

import models.Gift;
import models.mails.GiveMail;
import models.Request;
import models.User;
import play.db.ebean.Model;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.gift_give;
import views.html.requests;

import java.util.Map;

import static controllers.Users.currentUser;
import static java.lang.String.format;

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
        flash(Application.FLASH_MESSAGE_KEY, format("Vous souhaitez donner %s à %s. Un email a été envoyé à %s", gift.name, requester.name, requester.email));

        new GiveMail(request, data.get("description")[0]).send();
        gift.requests.stream().forEach(Model::delete);
        return redirect(routes.Requests.index());
    }

    public static Result request(Request request) {
        return ok(gift_give.render(currentUser(), request));
    }

}
