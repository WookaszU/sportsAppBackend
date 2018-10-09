package pl.edu.agh.sportsApp.websocket.stompevents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.security.Principal;

@Component
public class ConnectEvent implements ApplicationListener<SessionConnectEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ConnectEvent.class);

    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = event.getUser();
        logger.info("New socket connection.");
    }

}
