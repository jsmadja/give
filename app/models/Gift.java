package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.avaje.ebean.Expr.ne;

@Entity
@Table(name = "gifts")
public class Gift extends Model {

    public static final Finder<Long, Gift> find = new Finder<>(Long.class, Gift.class);

    @Id
    @GeneratedValue
    public Long id;

    @CreatedTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdatedTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public String name;

    @Lob
    @Column
    public String photoBase64;

    @ManyToOne
    public User giver;

    @OneToMany(mappedBy = "gift", cascade = CascadeType.ALL)
    public List<Request> requests;

    public boolean given;

    public static List<Gift> findAllGiftsFor(User user) {
        return find.where(ne("giver", user)).findList();
    }

}
