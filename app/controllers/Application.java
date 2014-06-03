package controllers;

import models.Profile;
import models.User;
import notifiers.Mails;
import org.bson.types.ObjectId;
import play.cache.Cache;
import play.data.validation.Valid;
import play.libs.OAuth2;
import play.mvc.Controller;

import java.util.List;

public class Application extends Controller {

    // The following keys correspond to a massageToday application
    // registered on Facebook, and associated with the massageToday.starpathit.cloudbees.net domain.
    public static OAuth2 FACEBOOK = new OAuth2(
            "https://graph.facebook.com/oauth/authorize",
            "https://graph.facebook.com/oauth/access_token",
            "630805913619847",
            "9aa4b6bab5b0b458935d9281ba5a5191"
    );


    public static void index() {
        render();
    }

    public static void search(String name) {
        List<User> users = User.q().filter("name", name).filter("role", User.RoleType.THERAPIST).asList();
        render("@searchResults", users);
    }

    public static void displayLogin() {
        render();
    }

    public static void channel() {
        render();
    }

    public static void forgotPassword() {
        render();
    }

    public static void register() {
        User user = connected();
        if (user != null) {
            render(user);
        }
        render();
    }

    public static void passwordReset() {
        render();
    }

    public static void searchResults() {
        render();
    }

    /**
     * Send an email with password reset instructions
     *
     * @param email
     */
    public static void sendForgotPasswordEmail(String email) {
        User user = User.find("byEmail", email).first();
        if (user != null) {
            user.verificationCode = UUIDUtil.shortUUID();
            Mails.forgotPassword(user);
            user.save();
            flash.success(user.name + ",  An email with password reset instructions is sent to your email address " + user.email + " Please check your email.");
        }
        displayLogin();
    }

    /**
     * Update contact with new password
     *
     * @param password
     * @param confirmPassword
     */
    public static void passwordResetFinish(String verificationCode, @Valid String password, @Valid String confirmPassword) {
        System.out.println("code=" + verificationCode + " password=" + password + " cnfPwd=" + confirmPassword);
        User contact = User.find("byVerificationCode", verificationCode).first();
        contact.password = password;
        contact.confirmEmail = confirmPassword;
        contact.save();
        flash.success(contact.name + ",  Your password has been reset. Please login with your new password.");
        displayLogin();
    }

    static User connected() {
        if (renderArgs.get("user") != null) {
            return renderArgs.get("user", User.class);
        }
        String email = session.get("user");
        if (email != null) {
            return User.find("byEmail", email).first();
        }
        return null;
    }

    /**
     * application login
     *
     * @param email
     * @param password
     */
    public static void login(String email, String password) {
        if (null == email || null == password) {
            displayLogin();
        }
        User user = User.find("byEmailAndPassword", email, password).first();
        if (user != null) {
            ObjectId userId = user.save().getId();
            session.put("user", user.email);
            session.put("userId", userId);
            if (null != user.role) {
                session.put("role", user.role.name());
                if(user.role.name().equals(User.RoleType.THERAPIST.name())){

                    Profile profile = Profile.find("byUserId", userId).first();
                    //renderBinary(profile.photo.get());
                    if (null == profile) {
                        render("@displayTherapistProfile", profile);
                    } else {
                        Data.displayTherapistSchedulesByUserId(userId.toString());
                    }
                }else{
                    render("@register", user);
                }
            }
        } else{
            flash.put("email", email);
            flash.error("Login failed");
            displayLogin();
        }


    }

    /**
     * facebook login
     */
    public static void auth() {
        if (OAuth2.isCodeResponse()) {
            User u = connected();
            OAuth2.Response response = FACEBOOK.retrieveAccessToken(authURL());
            u.access_token = response.accessToken;
            u.save();
            register();
        }
        FACEBOOK.retrieveVerificationCode(authURL());
    }

    static String authURL() {
        return play.mvc.Router.getFullUrl("Application.register");
    }

    public static void logout() {
        session.clear();
        flash.clear();
        index();
    }

    /**
     * Register or Save or Update a User
     *
     * @param user
     */
    public static void save(@Valid User user) {
        if (validation.hasErrors()) {
            if (request.isAjax()) error("Invalid value");
            render("@register", user);
        }
        User contact = User.find("byEmail", user.email).first();
        //save new user and review if exists
        if (contact == null) {
            System.out.println(user.role);
            ObjectId userId = user.save().getId();
            session.put("user", user.email);
            session.put("userId", userId);
            if (null != user.role) {
                session.put("role", user.role.name());
            }
            Mails.welcome(user);
            flash.success(user.name + ",  Sent a welcome email has been sent. Please check your email and complete verification.");
            session.put("role", user.role.name());
            if(user.role.name().equals(User.RoleType.THERAPIST.name())){

                Profile profile = Profile.find("byUserId", userId).first();
                //renderBinary(profile.photo.get());
                if (null == profile) {
                    render("@displayTherapistProfile", profile);
                } else {
                    Data.displayTherapistSchedulesByUserId(userId.toString());
                }
            }else{
                render("@register", user);
            }


        } else {
            //existing user
            if (connected() == null) {
                flash.error("A user with email already exists");
                render("@register", user);

            }
        }

    }

    /**
     * Sign-up through facebook Register or Save or Update a User
     *
     */
    public static void signUpThroughFacebook(String name, String email,  String confirmEmail, String access_token) {
        User contact = User.find("byEmail", email).first();
        //save new user and review if exists
        if (contact == null) {
            User user = new User(name, email);
            user.access_token = access_token;
            user.confirmEmail = confirmEmail;
            ObjectId userId = user.save().getId();
            session.put("user", user.email);
            session.put("userId", userId);
            if (null != user.role) {
                session.put("role", user.role.name());
            }
            Mails.welcome(user);
            flash.success(user.name + ",  Sent a welcome email has been sent. Please check your email and complete verification.");
            if(null != user.role && user.role.name().equals(User.RoleType.THERAPIST.name())){
                Profile profile = Profile.find("byUserId", userId).first();
                //renderBinary(profile.photo.get());
                if (null == profile) {
                    render("@displayTherapistProfile", profile);
                } else {
                    Data.displayTherapistSchedulesByUserId(userId.toString());
                }
            }else{
                render("@index", user);
            }

        } else{
            session.put("user", contact.email);
            session.put("userId", contact.save().getId());
            if (null != contact.role) {
                session.put("role", contact.role.name());
            }
            render("@index");
        }

    }

    public static String isValidUser(String email) {
        System.out.println("Ajax Requested Email" + email);
        User user = User.find("byEmail", email).first();
        if (user != null) {
            return "User already exists. Please enter a different email address";
        }
        return null;
    }

    public static String isUserAuthenticated(String loginEmail, String loginPassword) {
        System.out.println("Ajax Auth Request Email" + loginEmail);
        System.out.println("Ajax Auth Request Password" + loginPassword);
        User user = User.find("byEmailAndPassword", loginEmail, loginPassword).first();
        if (user == null) {
            return "Login Failed";
        }
        return null;
    }

    public static void displayTherapistProfile(String email, String  userId){
        session.put("user", email);
        session.put("userId", userId);
        Profile profile = Profile.find("byUserId", new ObjectId(userId)).first();
        if(null != profile)
         render(profile);
        else
         render();
    }




}