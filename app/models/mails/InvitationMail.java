package models.mails;

import java.util.HashMap;
import java.util.Map;

public class InvitationMail extends Mail {

    private final String inviterName;
    private final String website;

    public InvitationMail(String to, String inviterName, String website) {
        super(to);
        this.inviterName = inviterName;
        this.website = website;
    }

    @Override
    protected Map<String, String> values() {
        return new HashMap<String, String>() {{
            put("link", InvitationMail.this.website);
            put("inviter", InvitationMail.this.inviterName);
        }};
    }

    @Override
    protected String subject() {
        return "Bienvenue sur Give!";
    }

    @Override
    protected String templateName() {
        return "invitation";
    }

}
