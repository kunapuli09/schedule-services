package models;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import org.bson.types.ObjectId;
import play.data.validation.Required;
import play.modules.morphia.Model;

/**
 * hold date time, price range, length
 */
@play.modules.morphia.Model.NoAutoTimestamp
@Entity(noClassnameStored = true)
public class Massage extends Model {
    @Required
    @Embedded
    public Schedule schedule;

    @Required
    public ObjectId userId;

    @Required
    public ObjectId therapistId;

    public ServiceType massageType;

    public Float price;

    public String stripeToken;

    public Massage(ObjectId userId, ObjectId therapistId, ServiceType massageType, Float price) {
        this.userId = userId;
        this.therapistId = therapistId;
        this.massageType = massageType;
        this.price = price;
    }

    public Massage(Schedule schedule, ObjectId userId, ObjectId therapistId, ServiceType massageType, Float price, String stripeToken) {
        this.schedule = schedule;
        this.userId = userId;
        this.therapistId = therapistId;
        this.massageType = massageType;
        this.price = price;
        this.stripeToken = stripeToken;
    }
}
