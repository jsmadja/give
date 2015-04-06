package models;

import java.util.HashMap;
import java.util.Map;

public class RequestMail extends Mail {

    private final Request request;

    public RequestMail(Request request) {
        super(request.gift.giver.email);
        this.request = request;
    }

    @Override
    protected Map<String, String> values() {
        return new HashMap<String, String>() {{
            Request request = RequestMail.this.request;
            put("requester", request.requester.name);
            Gift gift = request.gift;
            put("photoBase64", gift.photoBase64);
            put("gift-name", gift.name);
            put("gift-description", gift.description);
            put("request-id", request.id.toString());
        }};
    }

    @Override
    protected String subject() {
        return request.requester.name + " est intéressé par " + request.gift.name;
    }

    @Override
    protected String templateName() {
        return "request";
    }

}
