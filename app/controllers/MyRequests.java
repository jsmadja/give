package controllers;

import models.Gift;
import models.Request;
import models.RequestMail;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.myrequests;

import java.util.Map;

import static controllers.Application.FLASH_MESSAGE_KEY;
import static controllers.Users.currentUser;
import static java.lang.String.format;

@Security.Authenticated(Secured.class)
public class MyRequests extends Controller {

    public static Result index() {
        return ok(myrequests.render(currentUser()));
    }

    public static Result addRequest() {
        Map<String, String[]> data = request().body().asFormUrlEncoded();
        Long id = Long.parseLong(data.get("giftId")[0]);
        Request request = new Request();
        request.gift = Gift.find.byId(id);
        request.requester = currentUser();
        request.save();
        new RequestMail(request).send();
        flash(FLASH_MESSAGE_KEY, format("Vous avez demandé un objet à %s. Un email a été envoyé !", request.gift.giver.name));
        return redirect(routes.Application.index());
    }

    public static Result removeRequest() {
        Map<String, String[]> data = request().body().asFormUrlEncoded();
        Long id = Long.parseLong(data.get("requestId")[0]);
        Request request = Request.find.byId(id);
        flash(FLASH_MESSAGE_KEY, "Vous avez supprimé votre demande");
        request.delete();
        return redirect(routes.MyRequests.index());
    }

}
