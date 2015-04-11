package models;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.feth.play.module.pa.user.EmailIdentity;
import com.feth.play.module.pa.user.NameIdentity;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.mvc.PathBindable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.avaje.ebean.Expr.*;
import static models.LinkType.INVITED;
import static models.LinkType.LINKED;

@Entity
@Table(name = "users")
public class User extends Model implements PathBindable<User> {

    public static final Finder<Long, User> find = new Finder<>(Long.class, User.class);

    @Id
    @GeneratedValue
    public Long id;
    @Constraints.Email
    // if you make this unique, keep in mind that users *must* merge/link their
    // accounts then on signup with additional providers
    // @Column(unique = true)
    public String email;
    public String name;
    public boolean active;
    public boolean emailValidated;

    @OneToMany(cascade = CascadeType.ALL)
    public List<LinkedAccount> linkedAccounts;

    @OneToMany(mappedBy = "giver")
    public List<Gift> gifts;

    @OneToMany(mappedBy = "requester")
    public List<Request> requests;

    @CreatedTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdatedTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "invitee")
    private List<Contact> contactsAsInvitee;

    @OneToMany(mappedBy = "inviter")
    private List<Contact> contactsAsInviter;

    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    public static boolean existsByAuthUserIdentity(final AuthUserIdentity identity) {
        return getAuthUserFind(identity).findRowCount() > 0;
    }

    private static ExpressionList<User> getAuthUserFind(final AuthUserIdentity identity) {
        return find.where().eq("active", true)
                .eq("linkedAccounts.providerUserId", identity.getId())
                .eq("linkedAccounts.providerKey", identity.getProvider());
    }

    public static User findByAuthUserIdentity(final AuthUserIdentity identity) {
        if (identity == null) {
            return null;
        }
        return getAuthUserFind(identity).findUnique();
    }

    public static User create(final AuthUser authUser) {
        User user = null;
        if (authUser instanceof EmailIdentity) {
            String email = ((EmailIdentity) authUser).getEmail();
            user = findByEmail(email);
        }
        if (user == null) {
            user = new User();
        }
        user.active = true;
        user.linkedAccounts = Collections.singletonList(LinkedAccount.create(authUser));

        if (authUser instanceof EmailIdentity && user.id == null) {
            final EmailIdentity identity = (EmailIdentity) authUser;
            // Remember, even when getting them from FB & Co., emails should be
            // verified within the application as a security breach there might
            // break your security as well!
            user.email = identity.getEmail();
            user.emailValidated = false;
        }

        if (authUser instanceof NameIdentity) {
            final NameIdentity identity = (NameIdentity) authUser;
            final String name = identity.getName();
            if (name != null && user.name == null) {
                user.name = name;
            }
        }

        user.save();
        return user;
    }

    public static User findByEmail(String email) {
        return find.where(eq("email", email)).findUnique();
    }

    public List<Request> getPendingRequests() {
        List<Request> requests = new ArrayList<>();
        for (Gift gift : gifts) {
            requests.addAll(gift.requests);
        }
        return requests;
    }

    public List<Gift> getCatalog() {
        List<Gift> availableGifts = new ArrayList<>();
        for (User linkedUser : getLinkedUsers()) {
            availableGifts.addAll(linkedUser.getAvailableGifts());
        }
        List<Gift> requestedGifts = findRequestedGifts();
        availableGifts.removeAll(requestedGifts);
        return availableGifts;
    }

    public List<Gift> findRequestedGifts() {
        List<Gift> gifts = new ArrayList<>();
        List<Request> requests = Request.find.where().eq("gift.given", false).eq("requester", this).findList();
        for (Request request : requests) {
            gifts.add(request.gift);
        }
        return gifts;
    }

    private List<Gift> getAvailableGifts() {
        return Gift.find.where().eq("giver", this).eq("given", false).findList();
    }

    public List<User> getLinkedUsers() {
        List<User> linkedUsers = new ArrayList<>();
        List<Contact> contacts = Contact.find.where(and(eq("type", LINKED), or(eq("invitee", this), eq("inviter", this)))).findList();
        for (Contact contact : contacts) {
            if (contact.invitee.equals(this)) {
                linkedUsers.add(contact.inviter);
            } else {
                linkedUsers.add(contact.invitee);
            }
        }
        return linkedUsers;
    }

    @Override
    public User bind(String key, String value) {
        return find.byId(Long.valueOf(value));
    }

    @Override
    public String unbind(String s) {
        return id.toString();
    }

    @Override
    public String javascriptUnbind() {
        return null;
    }

    public boolean hasInvited(User invitee) {
        return Contact.find.where().eq("inviter", this).eq("invitee", invitee).findRowCount() > 0;
    }

    public Contact getContactWith(User inviter) {
        Contact unique = Contact.find.where().eq("invitee", this).eq("inviter", inviter).findUnique();
        if(unique == null) {
            unique = Contact.find.where().eq("invitee", inviter).eq("inviter", this).findUnique();
        }
        return unique;
    }

    public List<Contact> getPendingContacts() {
        return Contact.find.where().eq("inviter", this).eq("type", INVITED).findList();
    }

    public List<Contact> getPendingInvitations() {
        return Contact.find.where().eq("invitee", this).eq("type", INVITED).findList();
    }

}