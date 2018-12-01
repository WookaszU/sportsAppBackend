package pl.edu.agh.sportsApp.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.sportsApp.model.notification.Notification;

public interface NotificationRepository <T extends Notification> extends JpaRepository<T, Long> {

}
