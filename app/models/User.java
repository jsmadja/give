package models;

import com.avaje.ebean.Expr;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static controllers.Users.currentUser;
import static models.Gift.findAllGiftsFor;

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

    public static boolean existsByAuthUserIdentity(final AuthUserIdentity identity) {
        final ExpressionList<User> exp = getAuthUserFind(identity);
        return exp.findRowCount() > 0;
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
        final User user = new User();
        user.active = true;
        user.linkedAccounts = Collections.singletonList(LinkedAccount.create(authUser));

        if (authUser instanceof EmailIdentity) {
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
            if (name != null) {
                user.name = name;
            }
        }

        user.save();
        return user;
    }

    public static List<Gift> getCatalog() {
        User user = currentUser();
        List<Gift> availableGifts = findAllGiftsFor(user);
        List<Gift> requestedGifts = Request.findRequestedGiftsOf(user);
        availableGifts.removeAll(requestedGifts);
        return availableGifts;
    }

    public static List<Request> getPendingRequests() {
        return Request.findFriendRequestsOf(currentUser());
    }

    public static List<User> getContacts() {
        return find.where(Expr.ne("id", currentUser().id)).findList();
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

}