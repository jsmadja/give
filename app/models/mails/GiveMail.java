package models.mails;

import models.Request;

import java.util.HashMap;
import java.util.Map;

public class GiveMail extends Mail {

    private final Request request;
    private final String description;

    public GiveMail(Request request, String description) {
        super(request.requester.email, request.gift.giver.email);
        this.request = request;
        this.description = description;
    }

    @Override
    protected Map<String, String> values() {
        return new HashMap<String, String>() {{
            Request request = GiveMail.this.request;
            put("giver", request.gift.giver.name);
            put("requester", request.requester.name);
            put("description", GiveMail.this.description);
        }};
    }

    @Override
    protected String subject() {
        return "Finalisation d'un don d'objet";
    }

    @Override
    protected String templateName() {
        return "give";
    }

}
