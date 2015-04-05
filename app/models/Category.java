package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "categories")
public class Category extends Model {

    public static final Finder<Long, Category> find = new Finder<>(Long.class, Category.class);

    @Id
    @GeneratedValue
    public Long id;

    @CreatedTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdatedTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Constraints.Required
    public String name;

}
