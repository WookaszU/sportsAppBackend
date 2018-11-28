package pl.edu.agh.sportsApp.model.notification;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.notifications.EventType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Entity(name = "notification")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
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

    public Notification(EventType eventType, LocalDateTime dateTime, Map<Long, User> relatedUsers) {
        this.eventType = eventType;
        this.dateTime = dateTime;
        this.relatedUsers = relatedUsers;
    }

}
