package pl.edu.agh.sportsApp.notifications.manager.updater;

import pl.edu.agh.sportsApp.model.notification.Notification;

import java.util.Set;

public interface INotificationUpdater {

    void saveNotificationIfRequired(Notification notification);

    void updateNotificationsDb(Set<Notification> updatedState);

}
