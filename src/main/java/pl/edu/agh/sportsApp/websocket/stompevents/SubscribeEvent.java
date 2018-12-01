package pl.edu.agh.sportsApp.websocket.stompevents;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import pl.edu.agh.sportsApp.notifications.AppEventHandler;
import pl.edu.agh.sportsApp.websocket.principal.SocketPrincipal;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscribeEvent implements ApplicationListener<SessionSubscribeEvent> {

    @NonNull
    AppEventHandler appEventHandler;

    @SuppressWarnings("all")
    public void onApplicationEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        if(sha.getHeader("simpDestination").equals("/user/queue/reply") && event.getUser() != null)
            appEventHandler.handleUserLoginEvent(((SocketPrincipal) event.getUser()).getId());
    }

}