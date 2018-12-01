package pl.edu.agh.sportsApp.notifications.manager.sender;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pl.edu.agh.sportsApp.model.notification.Notification;
import pl.edu.agh.sportsApp.websocket.sender.SocketSender;

import java.util.List;
import java.util.Set;

@Primary
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationSender implements INotificationSender {

    @NonNull
    SocketSender socketSender;

    public void sendStoredNotificationsToUser(Long userId, Set<Notification> userNotifications) {
        for(Notification notification: userNotifications)
            socketSender.sendToUser(userId.toString(), notification.mapToDTO());
    }

    public void sendNotificationToUsers(List<Long> usersIds, Notification notification) {
        for(Long userId: usersIds)
            socketSender.sendToUser(userId.toString(), notification.mapToDTO());
    }

}
