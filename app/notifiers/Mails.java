package notifiers;

import models.User;
import play.i18n.Messages;
import play.mvc.Mailer;

public class Mails extends Mailer {

    public static void welcome(User user) {
        String verificationCode = user.getId().toString();
        setSubject(Messages.get("welcomeEmail.subject", user.email));
        addRecipient(user.email);
        setFrom(Messages.get("adminEmail"));
        send(user, verificationCode);
    }

    public static void forgotPassword(User user) {
        setFrom(Messages.get("adminEmail"));
        setSubject(Messages.get("passwordEmail.subject"));
        addRecipient(user.email);
        send(user);
    }
}
