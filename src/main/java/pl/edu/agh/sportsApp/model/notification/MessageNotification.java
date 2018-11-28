package pl.edu.agh.sportsApp.model.notification;

import lombok.*;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.model.chat.Chat;
import pl.edu.agh.sportsApp.notifications.EventType;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "messageNotification")
@Getter
@Setter
public class MessageNotification extends Notification {

    @ManyToOne
    @JoinColumn(name = "chatId", referencedColumnName = "id")
    private Chat chat;

    @Builder
    public MessageNotification(LocalDateTime dateTime, Map<Long, User> relatedUsers, Chat chat) {
        super(EventType.NEW_MESSAGE, dateTime, relatedUsers);
        this.chat = chat;
    }

}
