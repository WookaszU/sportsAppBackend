package pl.edu.agh.sportsApp.notifications.manager.sender;

import pl.edu.agh.sportsApp.model.notification.Notification;

import java.util.List;
import java.util.Set;

public interface INotificationSender {

    void sendStoredNotificationsToUser(Long userId, Set<Notification> userNotifications);

    void sendNotificationToUsers(List<Long> usersIds, Notification notification);

}
