package controllers;

import models.Gift;
import models.Request;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.catalog;

import java.util.Map;

import static controllers.Users.currentUser;
import static models.Gift.findAllGiftsFor;

@Security.Authenticated(Secured.class)
public class Catalog extends Controller {

    public static Result index() {
        return ok(catalog.render(currentUser(), findAllGiftsFor(currentUser())));
    }

    public static Result addRequest() {
        Map<String, String[]> data = request().body().asFormUrlEncoded();
        Long id = Long.parseLong(data.get("giftId")[0]);
        Request request = new Request();
        request.gift = Gift.find.byId(id);
        request.requester = currentUser();
        request.save();
        return redirect(routes.Catalog.index());
    }

}
