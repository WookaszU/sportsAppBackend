package pl.edu.agh.sportsApp.model.notification;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import pl.edu.agh.sportsApp.dto.socket.MessageNotificationDTO;
import pl.edu.agh.sportsApp.model.chat.Chat;
import pl.edu.agh.sportsApp.notifications.EventType;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "messageNotification")
@Getter
@Setter
public class MessageNotification extends Notification {

    @ManyToOne
    @JoinColumn(name = "chatId", referencedColumnName = "id")
    @Cascade(CascadeType.SAVE_UPDATE)
    private Chat chat;

    @Builder
    public MessageNotification(LocalDateTime dateTime, Chat chat) {
        super(EventType.NEW_MESSAGE, dateTime);
        this.chat = chat;
    }

    @Override
    public MessageNotificationDTO mapToDTO() {
        return MessageNotificationDTO.builder()
                .eventType(this.getEventType())
                .chatId(this.getChat().getId())
                .dateTime(this.getDateTime())
                .build();
    }

}
