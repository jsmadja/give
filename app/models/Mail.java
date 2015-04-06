package models;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import play.Logger;
import play.Play;
import plugins.MailerPlugin;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static javax.mail.Message.RecipientType.TO;

public abstract class Mail {

    private final String to;

    public Mail(String to) {
        checkNotNull(to);
        this.to = to;
    }

    protected abstract String subject();

    protected abstract String templateName();

    public String text() {
        return build(template(Format.TEXT), values());
    }

    public String html() {
        return build(template(Format.HTML), values());
    }

    public void send() {
        sendMail();
    }

    private void sendMail() {
        if (Play.application().isDev()) {
            Logger.debug("to: " + to);
            Logger.debug("mail: " + text());
            //return;
        }
        try {
            Session mailSession = Session.getDefaultInstance(MailerPlugin.properties, MailerPlugin.authenticator);
            //mailSession.setDebug(true);
            Transport transport = mailSession.getTransport();
            MimeMessage message = new MimeMessage(mailSession);

            Multipart multipart = new MimeMultipart("alternative");

            BodyPart part1 = new MimeBodyPart();
            part1.setText(text());

            BodyPart part2 = new MimeBodyPart();
            part2.setContent(html(), "text/html");

            multipart.addBodyPart(part1);
            multipart.addBodyPart(part2);

            message.setContent(multipart);
            message.setFrom(new InternetAddress("julien.smadja@free.fr"));
            message.setSubject(subject());
            message.addRecipient(TO, new InternetAddress(to));

            transport.connect();
            transport.sendMessage(message, message.getRecipients(TO));
            transport.close();
        } catch (MessagingException e) {
            throw new IllegalStateException(e);
        }
    }

    private String build(String template, Map<String, String> values) {
        for (Map.Entry<String, String> value : values.entrySet()) {
            template = template.replace("$" + value.getKey(), Strings.nullToEmpty(value.getValue()));
        }
        return template;
    }

    private String template(Format format) {
        try {
            String filename = format("app/mails/%s.%s.%s", templateName(), "fr", format.extension());
            return Files.toString(new File(filename), Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    protected Map<String, String> values() {
        return new HashMap<String, String>();
    }

    public enum Format {
        TEXT("txt"), HTML("html");

        private String extension;

        Format(String extension) {
            this.extension = extension;
        }

        public String extension() {
            return extension;
        }
    }

}
