package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.avaje.ebean.Expr.eq;
import static com.avaje.ebean.Expr.ne;

@Entity
@Table(name = "requests")
public class Request extends Model {

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

    public static List<Request> findFriendRequestsOf(User user) {
        List<Request> requests = new ArrayList<>();
        for (Gift gift : user.gifts) {
            requests.addAll(gift.requests);
        }
        return requests;
    }

    public static List<Gift> findRequestedGiftsOf(User user) {
        List<Gift> gifts = new ArrayList<>();
        for (Request request : user.requests) {
            if (!gifts.contains(request.gift)) {
                gifts.add(request.gift);
            }
        }
        return gifts;
    }

    public static List<Request> findRequestsOf(User user) {
        return find.where(eq("requester", user)).findList();
    }
}
