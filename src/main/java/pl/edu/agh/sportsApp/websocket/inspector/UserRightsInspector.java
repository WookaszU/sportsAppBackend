package pl.edu.agh.sportsApp.websocket.inspector;

import java.security.Principal;

public interface UserRightsInspector {

    boolean hasUserSubscriptionRights(Principal principal, String topicDestination);

    boolean hasUserMessagingRights(Principal principal, String messageDestination);

}
