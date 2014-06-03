package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.*;
import org.bson.types.ObjectId;
import play.cache.Cache;
import play.data.validation.Valid;
import play.modules.paginate.ValuePaginator;
import play.mvc.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Data extends Controller {

    public static void updateTherapist(@Valid Profile profile){
        if (validation.hasErrors()) {
            if (request.isAjax())
                error("Invalid value");
            System.out.println("invalid");
            render("@Application.displayTherapistProfile", profile);
        }
        String userId = session.get("userId");
        session.put("userId", userId);
        ObjectId objectId = new ObjectId(userId);
        User user = User.findById(objectId);
        Profile dbProfile = Profile.find("byUserId",objectId).first();
        if (null == dbProfile) {
            System.out.println("not found user profile for "+objectId);
            dbProfile = new Profile(objectId, user.name, profile.licence, profile.insurance, profile.phone, profile.bio);
            dbProfile.photo = profile.photo;
            dbProfile.save();
        } else {
            System.out.println("found user profile for "+objectId);
            dbProfile.licence = profile.licence;
            dbProfile.insurance = profile.insurance;
            dbProfile.phone = profile.phone;
            dbProfile.bio = profile.bio;
            dbProfile.photo = profile.photo;
        }
        displayTherapistSchedulesByUserId(userId);
    }

    public static void addTherapistLocationForSchedule(@Valid Location workLocation) {
        List<Location> locations = null;
        String userId = session.get("userId");
        //return to the page the current location that has errors plus the a list of locations
        session.put("userId", userId);
        locations = getAllLocationsByUserId(userId);
        if (validation.hasErrors()) {
            if (request.isAjax())
                error("Invalid value");
            render("@displayTherapistScheduleProfile", locations);

        } else {
            //validation has no errors
            //this entire process is when user has entered a location with no validation errors
            if (locations == null) {
                //this is the first location for the user..user has no other valid locations already entered
                // user has no validation errors so save this to db
                locations = new ArrayList<Location>();
                workLocation.userId = new ObjectId(userId);
                workLocation.save();
                System.out.println("this is first location " + workLocation.name + " has no errors");
            } else {
                //this is not the first location for the user..user has other valid locations already entered
                // user has no validation errors so add this location to other existing locations and save this to db
                workLocation.userId = new ObjectId(userId);
                workLocation.save();
                locations.add(workLocation);
                System.out.println("user has previous locations so add this " + workLocation.name + " to existing locations");
            }
            locations = getAllLocationsByUserId(userId);
            render("@displayTherapistScheduleProfile", locations);
        }

    }

    public static void addTherapistLocationForService(@Valid Location workLocation) {
        List<Location> locations;
        String userId = session.get("userId");
        session.put("userId", userId);
        locations = getAllLocationsByUserId(userId);
        if (validation.hasErrors()) {
            if (request.isAjax())
                error("Invalid value");

            //return to the page the current location that has errors plus the a list of locations
            session.put("userId", userId);
            render("@displayTherapistServicesProfile", locations);

        } else {
            //validation has no errors
            //this entire process is when user has entered a location with no validation errors
            if (locations == null) {
                //this is the first location for the user..user has no other valid locations already entered
                // user has no validation errors so save this to db
                locations = new ArrayList<Location>();
                workLocation.userId = new ObjectId(userId);
                workLocation.save();
                locations.add(workLocation);
                System.out.println("this is first location " + workLocation.name + " has no errors");
            } else {
                //this is not the first location for the user..user has other valid locations already entered
                // user has no validation errors so add this location to other existing locations and save this to db
                workLocation.userId = new ObjectId(userId);
                workLocation.save();
                locations.add(workLocation);
                System.out.println("user has previous locations so add this " + workLocation.name + " to existing locations");

            }
            locations = getAllLocationsByUserId(userId);
            render("@displayTherapistServicesProfile", locations);
        }

    }

    public static void addTherapistLocation(@Valid Location workLocation) {
        ValuePaginator<Location> paginator = null;
        String userId = session.get("userId");
        session.put("userId", userId);
        List<Location> locations = getAllLocationsByUserId(userId);
        if (validation.hasErrors()) {
            if (request.isAjax())
                error("Invalid value");

            if (locations != null) {
                //this is not the first location for the user..user has other valid locations already entered
                // user has validation errors entering one of the subsequent locations
                paginator = new ValuePaginator<Location>(locations);
                System.out.println("user has previous locations but this location " + workLocation.name + " has errors");

            } else {
                //user has no valid location persisted yet. this got to be the first location
                paginator = new ValuePaginator<Location>(new ArrayList<Location>());
                System.out.println("user has no previous locations but this first location " + workLocation.name + " has errors");
            }
            //return to the page the current location that has errors plus the a list of locations
            render("@displayTherapistLocationProfile", paginator, locations);

        } else {
            //validation has no errors
            //this entire process is when user has entered a location with no validation errors
            if (locations == null) {
                //this is the first location for the user..user has no other valid locations already entered
                // user has no validation errors so save this to db
                locations = new ArrayList<Location>();
                workLocation.userId = new ObjectId(userId);
                workLocation.save();
                locations.add(workLocation);
                System.out.println("this is first location " + workLocation.name + " has no errors");
                paginator = new ValuePaginator<Location>(locations);
            } else {
                //this is not the first location for the user..user has other valid locations already entered
                // user has no validation errors so add this location to other existing locations and save this to db
                workLocation.userId = new ObjectId(userId);
                workLocation.save();
                locations.add(workLocation);
                System.out.println("user has previous locations so add this " + workLocation.name + " to existing locations");
                paginator = new ValuePaginator<Location>(getAllLocationsByUserId(userId));
            }
            render("@displayTherapistLocationProfile", paginator);
        }

    }

    public static void editLocation(String editAddress1, String editCity, String editState, String editZip, String locationId, String userId) {
        session.put("userId",userId);
        Location location = Location.findById(new ObjectId(locationId));
        location.address1 = editAddress1;
        location.city = editCity;
        location.state = editState;
        location.zip = Integer.valueOf(editZip);
        location.save();
        ValuePaginator<Location> paginator = new ValuePaginator<Location>(getAllLocationsByUserId(userId));
        render("@displayTherapistLocationProfile", paginator);
    }

    public static void updateTherapistSchedules(@Valid Schedule schedule){
        ValuePaginator<Schedule> scheduleValuePaginator = null;
        List<Schedule> schedules;
        List<Location> locations;
        flash.clear();
        String userId = session.get("userId");
        session.put("userId", userId);
        schedule.userId = new ObjectId(userId);
        if (schedule.locationId == null) {
            //profile can't be null for a signed in user
            flash.error("You need to have a location");
            render("@displayTherapistScheduleProfile");
        }
        locations = Location.find("byUserId", new ObjectId(userId)).fetchAll();
        if (validation.hasErrors()) {
            if (request.isAjax())
                error("Invalid value");
            schedules = new ArrayList<Schedule>();
            scheduleValuePaginator = new ValuePaginator<Schedule>(schedules);
            render("@displayTherapistScheduleProfile", scheduleValuePaginator, locations);

        } else {
            //validation has no errors
            //this entire process is when user has entered a schedule with no validation errors
            if(schedule.isGoodSchedule() == false){
                //profile can't be null for a signed in user
                flash.error("Schedules after 9PM and before 7AM of any day are not acceptable. Please re-enter");
                schedules = Schedule.find("byUserId", schedule.userId).fetchAll();
                scheduleValuePaginator = new ValuePaginator<Schedule>(schedules);
                render("@displayTherapistScheduleProfile", scheduleValuePaginator, locations, schedule);
                render("@displayTherapistScheduleProfile");
            }
            System.out.println(schedule.day);
            schedule.save();
            System.out.println("this is first schedule and has no errors");
            schedules = Schedule.find("byUserId", schedule.userId).fetchAll();
            scheduleValuePaginator = new ValuePaginator<Schedule>(schedules);
            render("@displayTherapistScheduleProfile", scheduleValuePaginator, locations);
        }

    }

    public static void updateTherapistServices(@Valid Service service) {
            ValuePaginator<Service> serviceValuePaginator = null;
        List<Service> services;
        List<Location> locations;
        String userId = session.get("userId");
        session.put("userId", userId);
        if (service.locationId == null) {
            //profile can't be null for a signed in user
            flash.error("You need to have a location");
            render("@displayTherapistServicesProfile");
        }
        locations = Location.find("byUserId", new ObjectId(userId)).fetchAll();
        if (validation.hasErrors()) {
            if (request.isAjax())
                error("Invalid value");
            services = new ArrayList<Service>();
            serviceValuePaginator = new ValuePaginator<Service>(services);
            render("@displayTherapistServicesProfile", serviceValuePaginator, locations);


        } else {
            service.userId = new ObjectId(userId);
            service.save();
            System.out.println("this is first service and has no errors");
            services = Service.find("byUserId", new ObjectId(userId)).fetchAll();
            serviceValuePaginator = new ValuePaginator<Service>(services);
            render("@displayTherapistServicesProfile", serviceValuePaginator, locations);
        }

    }
    public static void deleteSchedule(String scheduleId){
        System.out.println("Removing Schedule"+scheduleId);
        Schedule schedule = Schedule.findById(new ObjectId(scheduleId));
        schedule.delete();
        String userId = session.get("userId");
        session.put("userId", userId);
        List<Schedule> schedules = Schedule.find("byUserId", new ObjectId(userId)).fetchAll();
        ValuePaginator<Schedule> scheduleValuePaginator = new ValuePaginator<Schedule>(schedules);
        List<Location> locations = Location.find("byUserId", new ObjectId(userId)).fetchAll();
        render("@displayTherapistScheduleProfile", scheduleValuePaginator, locations);
    }

    public static void deleteService(String serviceId){
        System.out.println("Removing Service"+serviceId);
        Service service = Service.findById(new ObjectId(serviceId));
        service.delete();
        String userId = session.get("userId");
        session.put("userId", userId);
        List<Service> services = Service.find("byUserId", new ObjectId(userId)).fetchAll();
        ValuePaginator<Service> serviceValuePaginator = new ValuePaginator<Service>(services);
        List<Location> locations = Location.find("byUserId", new ObjectId(userId)).fetchAll();
        render("@displayTherapistServicesProfile", serviceValuePaginator, locations);
    }

    /**
     * Main navigation Work Locations link in main.html
     * @param userId
     */
    public static void displayTherapistLocationsByUserId(String userId){
        session.put("userId", userId);
        List<Location> locations = Location.find("byUserId", new ObjectId(userId)).fetchAll();
        ValuePaginator<Location> paginator = new ValuePaginator<Location>(locations);
        render("@displayTherapistLocationProfile",paginator);
    }

    /**
     * Main navigation Work Locations link in main.html
     * @param userId
     */
    public static void displayTherapistSchedulesByUserId(String userId){
        session.put("userId", userId);
        List<Location> locations = Location.find("byUserId", new ObjectId(userId)).fetchAll();
        List<Schedule> schedules = Schedule.find("byUserId", new ObjectId(userId)).fetchAll();
        ValuePaginator<Schedule> scheduleValuePaginator = new ValuePaginator<Schedule>(schedules);
        render("@displayTherapistScheduleProfile", scheduleValuePaginator, locations);
    }

    public static void displayTherapistServicesByUserId(String userId){
        session.put("userId", userId);
        List<Service> services = Service.find("byUserId", new ObjectId(userId)).fetchAll();
        List<Location> locations = Location.find("byUserId", new ObjectId(userId)).fetchAll();
        if(null == services){
            services = new ArrayList<Service>();
        }
        ValuePaginator<Service> serviceValuePaginator = new ValuePaginator<Service>(services);
        render("@displayTherapistServicesProfile", locations, serviceValuePaginator, locations);
    }

    public static List<Location> getAllLocationsByUserId(String userId){
        List<Location> locations = Location.find("byUserId", new ObjectId(userId)).fetchAll();
        return locations;
    }

    public static String isValidAddress(String name){
        Location location = Location.find("byName", name).first();
        if(null != location){
            return "Location "+ name+ " already exists";
        }
        return null;
    }

    public static void addLocation() {
        render();
    }

    public static void searchResults(String zip){
        ValuePaginator<Profile> profileValuePaginator = new ValuePaginator<Profile>(searchProfiles(zip));
        render(profileValuePaginator);
    }

    public static void checkout() {
        render();
    }
    public static void mobileSearch(String zip){
        List<Profile> profiles = searchProfiles(zip);
        renderJSON(profiles,new play.modules.morphia.utils.ObjectIdGsonAdapter());
    }

    private static List<Profile> searchProfiles(String zip){
        List<Location> locationsByZip = null;
        List<Profile> profiles = null;
        if(null == zip){
           locationsByZip = Location.findAll();
        } else{
           locationsByZip = Location.find("byZip", Integer.parseInt(zip)).fetchAll();
        }
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for(Location location: locationsByZip){
            userIds.add(location.userId);
        }
        if(userIds.size() > 0){
            profiles = Profile.q().filter("userId in", userIds).asList();
        }
        if(null == profiles){
            profiles = new ArrayList<Profile>();
        }
       return profiles;
    }


}
