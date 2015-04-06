package controllers;

import models.Gift;
import models.Request;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.myrequests;

import java.util.Map;

import static controllers.Users.currentUser;

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
        return redirect(routes.Catalog.index());
    }

    public static Result removeRequest() {
        Map<String, String[]> data = request().body().asFormUrlEncoded();
        Long id = Long.parseLong(data.get("requestId")[0]);
        Request request = Request.find.byId(id);
        request.delete();
        return redirect(routes.MyRequests.index());
    }

}
