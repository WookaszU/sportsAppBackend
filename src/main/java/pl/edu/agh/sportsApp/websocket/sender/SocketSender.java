package pl.edu.agh.sportsApp.websocket.sender;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import pl.edu.agh.sportsApp.websocket.sender.message.SocketMessage;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SocketSender {

    SimpMessageSendingOperations messagingTemplate;

    public void sendToUser(String name, SocketMessage message) {
        messagingTemplate.convertAndSendToUser(name, "/queue/reply", message);
    }

}
