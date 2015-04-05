package controllers;

import models.Gift;
import models.Request;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.requests;

import java.util.Map;

import static controllers.Users.currentUser;

@Security.Authenticated(Secured.class)
public class Requests extends Controller {

    public static Result index() {
        return ok(requests.render(currentUser(), Request.findAllRequestsFor(currentUser())));
    }

    public static Result give() {
        Map<String, String[]> stringMap = request().body().asFormUrlEncoded();
        Long requestId = Long.parseLong(stringMap.get("requestId")[0]);
        Request request = Request.find.byId(requestId);
        User receiver = request.requester;
        User user = currentUser();
        Gift gift = request.gift;
        gift.delete();
        return redirect(routes.Requests.index());
    }

}
