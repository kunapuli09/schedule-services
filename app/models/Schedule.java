package models;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import org.bson.types.ObjectId;
import play.data.validation.Required;
import play.modules.morphia.Model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/*
Schedule
*/
@play.modules.morphia.Model.NoAutoTimestamp
@Entity(noClassnameStored = true)
public class Schedule extends Model {

    @Required
    public String day;

    @Required
    public String startTime;

    @Required
    public String start;

    @Required
    public String endTime;

    @Required
    public String end;

    @Indexed
    public ObjectId locationId;

    @Indexed
    public ObjectId userId;


    public Schedule(final ObjectId locationId, final ObjectId userId, final String day, final String startTime, final String start, final String endTime, final String end) {
        this.locationId = locationId;
        this.userId = userId;
        this.day = day;
        this.startTime = startTime;
        this.start = start;
        this.endTime = endTime;
        this.end = end;
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

    public boolean isGoodSchedule(){
        Calendar inValidCalStart = GregorianCalendar.getInstance();
        inValidCalStart.set(Calendar.HOUR, 9);
        inValidCalStart.set(Calendar.MINUTE, 0);
        inValidCalStart.set(Calendar.AM_PM, Calendar.PM);

        Calendar inValidCalEnd = GregorianCalendar.getInstance();
        inValidCalEnd.add(Calendar.DATE, 1);
        inValidCalEnd.set(Calendar.HOUR, 7);
        inValidCalEnd.set(Calendar.MINUTE, 0);
        inValidCalEnd.set(Calendar.AM_PM, Calendar.AM);

        Calendar userEnteredStart = GregorianCalendar.getInstance();
        userEnteredStart.set(Calendar.HOUR, getHours(startTime));
        userEnteredStart.set(Calendar.MINUTE, getMinutes(startTime));
        userEnteredStart.set(Calendar.AM_PM, getAMorPM(start));

        Calendar userEnteredEnd = GregorianCalendar.getInstance();
        userEnteredEnd.set(Calendar.HOUR, getHours(endTime));
        userEnteredEnd.set(Calendar.MINUTE, getMinutes(endTime));
        userEnteredEnd.set(Calendar.AM_PM, getAMorPM(end));
        System.out.println("UserEntered Start Time "+userEnteredStart.getTime());
        System.out.println("UserEntered End Time "+userEnteredEnd.getTime());
        System.out.println("Invalid Time Begins At "+inValidCalStart.getTime());
        System.out.println("Invalid Time Ends At "+inValidCalEnd.getTime());

        boolean isStartBeforeInValidStart = userEnteredStart.before(inValidCalStart);
        boolean isEndAfterInValidEnd = userEnteredStart.before(inValidCalEnd);
        boolean isStartTimeGood =  isStartBeforeInValidStart && isEndAfterInValidEnd;
        boolean isEndBeforeInvalidStart = userEnteredEnd.before(inValidCalStart);
        boolean isEndAfterInvalidEnd = userEnteredEnd.before(inValidCalEnd);
        boolean isEndTimeGood =  isEndBeforeInvalidStart&&isEndAfterInvalidEnd;
        boolean isEndAfterStart = userEnteredStart.before(userEnteredEnd);

        if(isStartTimeGood && isEndTimeGood && isEndAfterStart){
            return true;
        }else {
            return false;
        }
    }

    private int getHours(String time){
        StringTokenizer stringTokenizer = new StringTokenizer(time, ":");
        return Integer.parseInt(stringTokenizer.nextToken());
    }
    private int getMinutes(String time){
        StringTokenizer stringTokenizer = new StringTokenizer(time,":");
        stringTokenizer.nextToken();
        String minutes = stringTokenizer.nextToken();
        return Integer.parseInt(minutes);
    }

    private int getAMorPM(String amOrPm){
        if(amOrPm.equalsIgnoreCase("AM")){
            return Calendar.AM;
        }else{
            return Calendar.PM;
        }

    }
}
