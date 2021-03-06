package models;

import com.feth.play.module.pa.user.AuthUser;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "linked_accounts")
public class LinkedAccount extends Model {

    public static final Finder<Long, LinkedAccount> find = new Finder<>(Long.class, LinkedAccount.class);

    @Id
    public Long id;
    @ManyToOne
    public User user;
    public String providerUserId;
    public String providerKey;

    public static LinkedAccount findByProviderKey(final User user, String key) {
        return find.where().eq("user", user).eq("providerKey", key).findUnique();
    }

    public static LinkedAccount create(final AuthUser authUser) {
        LinkedAccount ret = new LinkedAccount();
        ret.update(authUser);
        return ret;
    }

    public static LinkedAccount create(final LinkedAccount acc) {
        LinkedAccount ret = new LinkedAccount();
        ret.providerKey = acc.providerKey;
        ret.providerUserId = acc.providerUserId;
        return ret;
    }

    public void update(final AuthUser authUser) {
        this.providerKey = authUser.getProvider();
        this.providerUserId = authUser.getId();
    }
}