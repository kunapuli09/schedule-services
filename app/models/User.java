package models;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import play.data.validation.*;
import play.modules.morphia.Model;

/**
 * massageToday user
 */
@play.modules.morphia.Model.NoAutoTimestamp
@Entity(noClassnameStored = true)
public class User extends Model {

    public static enum RoleType {
        GUEST, THERAPIST, SUPER_USER
    }

    @Required
    @MinSize(4)
    public String name;

    @Required
    @Email
    @Indexed(unique = true)
    public String email;

    @Required
    @Equals("email")
    public String confirmEmail;

    @Required
    @Password
    @MinSize(6)
    public String password;

    @Required
    public RoleType role;

    public String verificationCode;

    //facebook access token
    public String access_token;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, String password, String confirmEmail) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmEmail = confirmEmail;
    }

    public User(String name, String email, String password, String confirmEmail, RoleType role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmEmail = confirmEmail;
        this.role = role;
    }
}

