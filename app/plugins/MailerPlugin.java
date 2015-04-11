package plugins;

import play.Application;
import play.Logger;
import play.Plugin;

import javax.mail.PasswordAuthentication;
import java.util.Properties;

public class MailerPlugin extends Plugin {

    public static final String SMTP_HOST_NAME = "mail.smtp.hostname";
    public static final String SMTP_AUTH_USER = "mail.smtp.auth.user";
    public static final String SMTP_AUTH_PWD = "mail.smtp.auth.pwd";

    private final Application application;

    public static SMTPAuthenticator authenticator;
    public static Properties properties;

    public MailerPlugin(Application application) {
        this.application = application;
    }

    @Override
    public void onStart() {
        properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", application.configuration().getString(SMTP_HOST_NAME));
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.starttls.enable", true); // added this line
        properties.put("mail.smtp.auth", "true");
        authenticator = new SMTPAuthenticator();
        Logger.info("Using Mailer: " + application.configuration().getString(SMTP_HOST_NAME));
    }

    @Override
    public boolean enabled() {
        if(true) {
            return true;
        }
        return application.configuration().keys().contains(SMTP_HOST_NAME) &&
                application.configuration().keys().contains(SMTP_AUTH_USER) &&
                application.configuration().keys().contains(SMTP_AUTH_PWD);
    }

    public static boolean isEnabled() {
        return authenticator != null;
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            String username = application.configuration().getString(SMTP_AUTH_USER);
            String password = application.configuration().getString(SMTP_AUTH_PWD);
            return new PasswordAuthentication(username, password);
        }
    }

}
