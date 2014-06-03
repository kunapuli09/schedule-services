package models;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.gson.annotations.Expose;
import org.bson.types.ObjectId;
import play.data.validation.Phone;
import play.data.validation.Required;
import play.modules.morphia.Model;

/**
 * MassageToday user
 */
@play.modules.morphia.Model.NoAutoTimestamp
@Entity(noClassnameStored = true)
public class Profile extends Model {

    @Required
    public String licence;

    @Required
    public String insurance;

    @Indexed
    public ObjectId userId;

    public String name;

    @Required
    @Phone
    @Expose
    public String phone;

    public String bio;

    public String modalities;

    public String photo;

    public Profile(final ObjectId userId, String name, String licence, String insurance, String phone, String bio) {
        this.userId = userId;
        this.name = name;
        this.licence = licence;
        this.insurance = insurance;
        this.phone = phone;
        this.bio = bio;

    }
}

