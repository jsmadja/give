package models;

import java.util.HashMap;
import java.util.Map;

public class GiveMail extends Mail {

    private final Request request;

    public GiveMail(Request request) {
        super(request.requester.email);
        this.request = request;
    }

    @Override
    protected Map<String, String> values() {
        return new HashMap<String, String>() {{
            Request request = GiveMail.this.request;
            put("giver", request.gift.giver.name);
            Gift gift = request.gift;
            put("gift-name", gift.name);
        }};
    }

    @Override
    protected String subject() {
        return request.gift.giver.name + " vous donne " + request.gift.name;
    }

    @Override
    protected String templateName() {
        return "give";
    }

}
