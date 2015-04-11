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

import static controllers.Application.FLASH_ERROR_KEY;
import static controllers.Application.FLASH_MESSAGE_KEY;
import static models.LinkType.LINKED;
import static models.LinkType.REFUSED;

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
        sendInvitationTo(email);
        return index();
    }

    private static void sendInvitationTo(String email) {
        User invitee = User.findByEmail(email);
        User inviter = currentUser();
        if (invitee == null) {
            sendInvitationToNewUser(email, inviter);
        } else {
            if (invitee.hasInvited(inviter)) {
                linkWithExistingUser(email, invitee, inviter);
            } else if (inviter.hasInvited(invitee)) {
                flash(FLASH_ERROR_KEY, "Vous avez déjà invité " + email);
            } else {
                sendInvitationToExistingUser(email, invitee, inviter);
            }
        }
    }

    private static void sendInvitationToExistingUser(String email, User invitee, User inviter) {
        Contact contact = new Contact(inviter, invitee, LinkType.INVITED);
        contact.save();
        flash(FLASH_MESSAGE_KEY, "Une invitation a été envoyée à " + email);
    }

    private static void linkWithExistingUser(String email, User invitee, User inviter) {
        Contact contact = invitee.getContactWith(inviter);
        contact.type = LINKED;
        contact.save();
        flash(FLASH_MESSAGE_KEY, "Vous avez désormais accès au catalogue de " + email);
    }

    private static void sendInvitationToNewUser(String email, User inviter) {
        User invitee = new User(email);
        invitee.save();
        sendInvitationToExistingUser(email, invitee, inviter);
    }

    public static Result acceptInvitation(Contact contact) {
        contact.type = LINKED;
        contact.save();
        flash(FLASH_MESSAGE_KEY, "Vous avez désormais accès au catalogue de " + contact.inviter.name);
        return index();
    }

    public static Result declineInvitation(Contact contact) {
        contact.type = REFUSED;
        contact.save();
        flash(FLASH_MESSAGE_KEY, "Vous avez refusé d'être connecté(e) à " + contact.inviter.name);
        return index();
    }

}
