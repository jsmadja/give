import controllers.Application;
import models.InvitationMail;
import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;

public class IntegrationTest {

    @Test
    public void sendMail() {
        running(fakeApplication(inMemoryDatabase("test")), new Runnable() {
            public void run() {
                //Application.mail();
            }
        });
    }

}
