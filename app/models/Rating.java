package models;

import com.google.code.morphia.annotations.Embedded;
import play.data.validation.Required;

/**
 * various rating metrics
 */
@Embedded
public class Rating {

    public static enum RatingType {
       PRICE("Price"), CUSTOMER_SERVICE("Customer Service"), SECURITY("Information is Secure");

        private final String desc;

        RatingType(String description) {
            this.desc = description;
        }

        public String description() {
            return desc;
        }

    }

    public final RatingType ratingType;

    public final int rating;

    public Rating(RatingType ratingType, int rating) {
        this.ratingType = ratingType;
        this.rating = rating;
    }
}
