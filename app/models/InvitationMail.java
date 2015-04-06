package models;

import java.util.HashMap;
import java.util.Map;

public class InvitationMail extends Mail {

    public InvitationMail(String to) {
        super(to);
    }

    @Override
    protected Map<String, String> values() {
        return new HashMap<String, String>() {{
            put("link", "http://www.give.me");
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
