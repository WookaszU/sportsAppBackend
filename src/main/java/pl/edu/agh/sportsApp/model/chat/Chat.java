package pl.edu.agh.sportsApp.model.chat;

import lombok.*;
import pl.edu.agh.sportsApp.model.Message;
import pl.edu.agh.sportsApp.model.notification.Notification;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"notifications", "messages"})
@Entity(name = "chat")
@Inheritance(strategy = InheritanceType.JOINED)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Message> messages = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Notification> notifications = new HashSet<>();

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public void removeNotification(Notification notification) {
        notifications.remove(notification);
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void removeMessage(Message message) {
        messages.remove(message);
    }

}
