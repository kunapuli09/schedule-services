package models;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import org.bson.types.ObjectId;
import play.data.validation.*;
import play.modules.morphia.Model;

import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;

/*
Could be a spa or any other location
*/
@play.modules.morphia.Model.NoAutoTimestamp
@Entity(noClassnameStored = true)
public class Location extends Model {

    @Required
    @Unique
    @Indexed
    public String name;

    @Required
    public String address1;

    public String address2;

    @Required
    public String city;

    @Required
    //@Match("[A-Z]{2}")
    public String state;

    @Required
    //@Match("[0-9]{5}")
    @Indexed
    public Integer zip;

    @Indexed
    public ObjectId userId;


    //TODO add longitude and latitude


    public Location(final ObjectId userId, String name, String address1, String city, String state, Integer zip) {
        this.name = name;
        this.address1 = address1;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
    public Location(final ObjectId userId, String name, String address1, String address2, String city, String state, Integer zip) {
        this.name = name;
        this.address1 = address1;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    @Override
    public String toString() {
        return "name=" + name +
                ", address1='" + address1 +
                ", address2='" + address2 +
                ", city='" + city +
                ", state='" + state +
                ", zip=" + zip ;
    }
    /*public static Location createLocationFromString(String location){
        StringTokenizer tokenizer = new StringTokenizer(",");
        String name = null;
        String address1 = null;
        String address2 = null;
        String city = null;;
        String state = null;
        Integer zip = null;
        while(tokenizer.hasMoreElements()){
            String token = (String)tokenizer.nextElement();
            if(token.startsWith("name=")){
                name = token.replaceFirst("name=","");
            }else if(token.startsWith("address1")){
                address1 = token.replaceFirst("address1=","");
            }else if(token.startsWith("address2")){
                address2 = token.replaceFirst("address2=","");
            }else if(token.startsWith("city")){
                city = token.replaceFirst("city=","");
            } else if(token.startsWith("state")){
                state = token.replaceFirst("state=","");
            }else if(token.startsWith("zip")){
                zip = Integer.valueOf(token.replaceFirst("zip=",""));
            }
        }
        System.out.println(name);
        return new Location(name,address1,address2,city,state,zip);
    }
*/
}
