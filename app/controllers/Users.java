package controllers;

import com.feth.play.module.pa.PlayAuthenticate;
import models.Contact;
import models.LinkType;
import models.User;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.users;
import views.html.users_read;

import java.io.IOException;
import java.util.Map;

public class Users extends Controller {

    public static Result index() {
        return ok(users.render(currentUser()));
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

    public static Result invite() throws IOException {
        Map<String, String[]> data = request().body().asFormUrlEncoded();
        String email = data.get("email")[0].toLowerCase();

        User invitee = User.findByEmail(email);
        User inviter = currentUser();
        if (invitee == null) {
            invitee = new User(email);
            invitee.save();
            Contact contact = new Contact(inviter, invitee, LinkType.INVITED);
            contact.save();
        } else {
            if (invitee.hasInvited(inviter)) {
                Contact contact = invitee.getContactOf(inviter);
                contact.type = LinkType.LINKED;
                contact.save();
            } else {
                Contact contact = new Contact(inviter, invitee, LinkType.INVITED);
                contact.save();
            }
        }
        flash(Application.FLASH_MESSAGE_KEY, "Une invitation a été envoyée à " + email);

        return index();
    }

    public static Result acceptInvitation(Contact contact) {
        contact.type = LinkType.LINKED;
        contact.save();
        flash(Application.FLASH_MESSAGE_KEY, "Vous avez désormais accès au catalogue de " + contact.inviter.name);
        return index();
    }

    public static Result declineInvitation(Contact contact) {
        contact.type = LinkType.REFUSED;
        contact.save();
        flash(Application.FLASH_MESSAGE_KEY, "Vous avez refusé d'être connecté(e) à " + contact.inviter.name);
        return index();
    }

}
