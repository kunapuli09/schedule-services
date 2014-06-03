package models;

/**
* Created with IntelliJ IDEA.
* User: kunapuli09
* Date: 8/26/13
* Time: 4:42 PM
* To change this template use File | Settings | File Templates.
*/
public enum DurationType {
    THIRTY_MIN("30 Minutes"),ONE_HOUR("One Hour");
    private final String desc;

    DurationType(String description) {
        this.desc = description;
    }

    public String description() {
        return desc;
    }
}
