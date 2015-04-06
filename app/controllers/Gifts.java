package controllers;

import com.ning.http.util.Base64;
import models.Category;
import models.Gift;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.gifts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static controllers.Users.currentUser;

@Security.Authenticated(Secured.class)
public class Gifts extends Controller {

    public static Result index() {
        return ok(gifts.render(currentUser(), Category.find.all()));
    }

    public static Result addGift() throws IOException {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart picture = body.getFile("photo");

        Map<String, String[]> data = body.asFormUrlEncoded();

        Gift gift = new Gift();
        gift.giver = currentUser();
        gift.name = data.get("name")[0];
        gift.description = data.get("description")[0];
        gift.category = Category.find.byId(Long.parseLong(data.get("category")[0]));

        if (picture != null) {
            File file = picture.getFile();
            gift.photoBase64 = Base64.encode(Files.readAllBytes(file.toPath()));
        }
        gift.save();
        return redirect(routes.Gifts.index());
    }

    public static Result removeGift() throws IOException {
        Map<String, String[]> data = request().body().asFormUrlEncoded();
        Long id = Long.parseLong(data.get("giftId")[0]);
        Gift gift = Gift.find.byId(id);
        gift.delete();
        return redirect(routes.Gifts.index());
    }

}
