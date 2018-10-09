package pl.edu.agh.sportsApp.websocket.stompevents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
public class DisconnectEvent implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DisconnectEvent.class);

    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = event.getUser();
        logger.info("User socket disconnect.");
    }

}