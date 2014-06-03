package models;

/**
* Created with IntelliJ IDEA.
* User: kunapuli09
* Date: 8/26/13
* Time: 4:42 PM
* To change this template use File | Settings | File Templates.
*/
public enum ServiceType {
    DEEP_TISSUE_MASSAGE("Deep Tissue Massage"), SWEDISH_MASSAGE("Swedish Massage");

    private final String desc;

    ServiceType(String description) {
        this.desc = description;
    }

    public String description() {
        return desc;
    }

}
