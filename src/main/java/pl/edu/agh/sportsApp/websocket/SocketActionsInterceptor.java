package pl.edu.agh.sportsApp.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import pl.edu.agh.sportsApp.websocket.inspector.UserRightsInspector;

@Component
public class SocketActionsInterceptor implements ChannelInterceptor {

    private UserRightsInspector rightsInspector;

    @Autowired
    public SocketActionsInterceptor(UserRightsInspector rightsInspector) {
        this.rightsInspector = rightsInspector;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            if (!rightsInspector.hasUserSubscriptionRights(headerAccessor.getUser(), headerAccessor.getDestination())) {
                return null;
            }
        } else if (StompCommand.SEND.equals(headerAccessor.getCommand())) {
            if (!rightsInspector.hasUserMessagingRights(headerAccessor.getUser(), headerAccessor.getDestination())) {
                return null;
            }
        }

        return message;
    }

}

