package pl.edu.agh.sportsApp.websocket.inspector;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pl.edu.agh.sportsApp.model.Event;
import pl.edu.agh.sportsApp.service.EventService;
import pl.edu.agh.sportsApp.websocket.principal.SocketPrincipal;

import java.security.Principal;
import java.util.Optional;

@Primary
@Component
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserInspector implements UserRightsInspector {

    EventService eventService;

    public boolean hasUserSubscriptionRights(Principal principal, String topicDestination) {
        //    /user/queue/reply
        if(topicDestination.equals("/user/queue/reply"))
            return true;

        String[] path = topicDestination.split("/");

        //    /topic/{chatId}
        if(path.length == 3 || path[1].equals("topic")) {
            Optional<Event> eventOpt = eventService.findEventById(Long.parseLong(path[2]));
            return eventOpt.isPresent() &&
                    eventOpt.get().getParticipantIds().contains(((SocketPrincipal) principal).getId());
        }

        return false;   // wrong path
    }

    public boolean hasUserMessagingRights(Principal principal, String messageDestination) {
        String[] path = messageDestination.split("/");

        //  /app/chat/{chatId}
        if(messageDestination.startsWith("/app/chat/") && path.length != 4) {
            Optional<Event> eventOpt = eventService.findEventById(Long.parseLong(path[2]));
            return eventOpt.isPresent() &&
                    eventOpt.get().getParticipantIds().contains(((SocketPrincipal) principal).getId());
        }

        return false;   // wrong path
    }

}
