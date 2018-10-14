package pl.edu.agh.sportsApp.websocket.inspector;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Primary
@Component
public class UserInspector implements UserRightsInspector {

    public boolean hasUserSubscriptionRights(Principal principal, String topicDestination) {
        // TODO
        return true;
    }

    public boolean hasUserMessagingRights(Principal principal, String messageDestination) {
        // TODO
        return true;
    }

}
