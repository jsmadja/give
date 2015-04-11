package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.db.ebean.Model;
import play.mvc.PathBindable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "contacts")
public class Contact extends Model implements PathBindable<Contact> {

    @ManyToOne
    public final User inviter;

    @ManyToOne
    public final User invitee;

    public LinkType type;

    @Id
    @GeneratedValue
    public Long id;

    @CreatedTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdatedTimestamp
    @Column(name = "updated_at")
    public Date updatedAt;

    public static final Finder<Long, Contact> find = new Finder<>(Long.class, Contact.class);

    public Contact(User inviter, User invitee, LinkType type) {
        this.inviter = inviter;
        this.invitee = invitee;
        this.type = type;
    }

    @Override
    public Contact bind(String key, String value) {
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
