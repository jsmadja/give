package models;

import java.util.HashMap;
import java.util.Map;

public class RequestMail extends Mail {

    private final Request request;

    public RequestMail(Request request) {
        super(request.gift.giver.email, request.requester.email);
        this.request = request;
    }

    @Override
    protected Map<String, String> values() {
        return new HashMap<String, String>() {{
            Request request = RequestMail.this.request;
            Gift gift = request.gift;
            put("giver", gift.giver.name);
            put("requester", request.requester.name);
            put("photoBase64", gift.photoBase64);
            put("gift-name", gift.name);
            put("request-id", request.id.toString());
        }};
    }

    @Override
    protected String subject() {
        return request.requester.name + " est intéressé(e) par un de vos objets";
    }

    @Override
    protected String templateName() {
        return "request";
    }

}
