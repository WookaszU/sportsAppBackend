package pl.edu.agh.sportsApp.dto.socket;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pl.edu.agh.sportsApp.notifications.EventType;
import pl.edu.agh.sportsApp.websocket.sender.message.SocketMessage;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageNotificationDTO implements SocketMessage {

    EventType eventType;

    LocalDateTime dateTime;

    Long chatId;

}
