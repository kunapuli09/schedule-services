package models;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import org.bson.types.ObjectId;
import play.data.validation.Required;
import play.modules.morphia.Model;

@play.modules.morphia.Model.NoAutoTimestamp
@Entity(noClassnameStored = true)
public class Service extends Model {

    public ServiceType serviceType;

    @Required
    //@Match("^\\d+(,\\d{1,2})?$")
    public Float price;

    public DurationType durationType;

    @Indexed
    public ObjectId locationId;

    @Indexed
    public ObjectId userId;

    public Service(final ObjectId locationId, final ObjectId userId, ServiceType serviceType, Float price, DurationType durationType){
        this.locationId = locationId;
        this.userId = userId;
        this.serviceType = serviceType;
        this.price = price;
        this.durationType = durationType;
    }
    public Location getLocation() {
        if (null == locationId) {
            return null;
        } else {
            return Location.findById(locationId);
        }
    }

    public User getUser() {
        if (null == userId) {
            return null;
        } else {
            return Location.findById(userId);
        }
    }

    public String getService(){
        return this.serviceType.description();
    }

    public String getDuration(){
        return this.durationType.description();
    }

}
