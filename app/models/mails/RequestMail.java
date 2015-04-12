package models.mails;

import models.Gift;
import models.Request;

import java.util.HashMap;
import java.util.Map;

public class RequestMail extends Mail {

    private final Request request;
    private final String website;

    public RequestMail(Request request, String website) {
        super(request.gift.giver.email);
        this.request = request;
        this.website = website;
    }

    @Override
    protected Map<String, String> values() {
        return new HashMap<String, String>() {{
            Request request = RequestMail.this.request;
            Gift gift = request.gift;
            put("giver", gift.giver.name);
            put("requester", request.requester.name);
            put("website", RequestMail.this.website);
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
