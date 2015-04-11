package controllers;

import com.ning.http.util.Base64;
import models.Gift;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.gifts;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static controllers.Application.FLASH_ERROR_KEY;
import static controllers.Application.FLASH_MESSAGE_KEY;
import static controllers.Users.currentUser;

@Security.Authenticated(Secured.class)
public class Gifts extends Controller {

    public static Result index() {
        return ok(gifts.render(currentUser()));
    }

    public static Result addGift() throws IOException {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart picture = body.getFile("photo");
        Map<String, String[]> data = body.asFormUrlEncoded();
        String name = data.get("name")[0];
        String photowebcam = data.get("photowebcam")[0];
        createNewGift(picture, name, photowebcam);
        return redirect(routes.Gifts.index());
    }

    private static void createNewGift(Http.MultipartFormData.FilePart picture, String name, String photowebcam) throws IOException {
        Gift gift = new Gift();
        gift.giver = currentUser();
        gift.name = name;
        if (!photowebcam.isEmpty()) {
            gift.photoBase64 = photowebcam.split("base64,")[1];
        } else if (picture != null) {
            gift.photoBase64 = Base64.encode(Files.readAllBytes(picture.getFile().toPath()));
        }
        if (gift.photoBase64 == null) {
            flash(FLASH_ERROR_KEY, "Vous devez obligatoirement ajouter une photo de l'objet à donner");
        } else {
            gift.save();
            flash(FLASH_MESSAGE_KEY, "Vous avez ajouté un nouvel objet à votre catalogue");
        }
    }

    public static Result removeGift() throws IOException {
        Map<String, String[]> data = request().body().asFormUrlEncoded();
        Long id = Long.parseLong(data.get("giftId")[0]);
        Gift gift = Gift.find.byId(id);
        gift.delete();
        flash(FLASH_MESSAGE_KEY, "Vous avez supprimé un objet de votre catalogue");
        return redirect(routes.Gifts.index());
    }

}
