package models.mails;

import models.Gift;
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
            Gift gift = request.gift;
            put("gift-name", gift.name);
            put("description", GiveMail.this.description);
        }};
    }

    @Override
    protected String subject() {
        return request.gift.giver.name + " vous donne un objet";
    }

    @Override
    protected String templateName() {
        return "give";
    }

}
