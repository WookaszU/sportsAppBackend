package pl.edu.agh.sportsApp.model.notification;

import lombok.*;
import org.hibernate.annotations.Cascade;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.notifications.EventType;
import pl.edu.agh.sportsApp.dto.socket.MessageNotificationDTO;
import pl.edu.agh.sportsApp.websocket.sender.message.SocketMessage;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Entity(name = "notification")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
public class Notification {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private EventType eventType;

    private LocalDateTime dateTime;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinTable(
            name = "NotificationUser",
            joinColumns = {@JoinColumn(name = "notification_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    @Builder.Default
    @MapKeyColumn(name = "id")
    private Map<Long, User> relatedUsers = new HashMap<>();

    public Notification(EventType eventType, LocalDateTime dateTime) {
        this.eventType = eventType;
        this.dateTime = dateTime;
    }

    public void addRelatedUser(User user) {
        relatedUsers.put(user.getId(), user);
    }

    public void removeRelatedUser(Long userId) {
        relatedUsers.remove(userId);
    }

    public SocketMessage mapToDTO() {
        return MessageNotificationDTO.builder()
                .eventType(this.getEventType())
                .dateTime(this.getDateTime())
                .build();
    }

}
