import play.test.*;
import play.jobs.*;
import models.*;

@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        // Load default data if the database is empty
        //Fixtures.load("data.yml");

    }

}