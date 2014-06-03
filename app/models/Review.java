package models;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Reference;
import org.bson.types.ObjectId;
import play.data.validation.Email;
import play.data.validation.Password;
import play.data.validation.Phone;
import play.data.validation.Required;
import play.modules.morphia.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MassageToday user
 */
@play.modules.morphia.Model.NoAutoTimestamp
@Entity(noClassnameStored = true)
public class Review extends Model {

    public Date reviewDate;

    @Embedded
    public List<Rating> ratings = new ArrayList<Rating>();

    @Required
    public String text;

    public String title;

    public String userName;

    @Indexed
    public ObjectId userId;

    @Indexed
    public ObjectId therapistId;

    //number of helpful votes on this review
    public int helpfulVotes;

    //userIds who have voted to the review
    public List<ObjectId> voterIds = new ArrayList<ObjectId>();

    public Review(String userName, String text, List<Rating> ratings, ObjectId userId, ObjectId therapistId) {
        this.userName = userName;
        this.text = text;
        this.ratings = ratings;
        this.reviewDate = new Date();
        this.userId = userId;
        this.therapistId = therapistId;
    }

    public User getTherapist() {
        if (null == therapistId) {
            return null;
        } else {
            return User.findById(therapistId);
        }
    }

    public User getUser() {
        if (null == userId) {
            return null;
        } else {
            return User.findById(userId);
        }
    }
}

