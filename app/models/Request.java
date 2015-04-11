package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.db.ebean.Model;
import play.mvc.PathBindable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.avaje.ebean.Expr.eq;
import static com.avaje.ebean.Expr.ne;

@Entity
@Table(name = "requests")
public class Request extends Model implements PathBindable<Request> {

    public static final Model.Finder<Long, Request> find = new Finder<>(Long.class, Request.class);

    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne
    public User requester;

    @ManyToOne
    public Gift gift;

    @CreatedTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdatedTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Override
    public Request bind(String key, String value) {
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
