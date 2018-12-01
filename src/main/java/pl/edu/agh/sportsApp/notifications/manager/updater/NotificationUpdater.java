package pl.edu.agh.sportsApp.notifications.manager.updater;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pl.edu.agh.sportsApp.model.notification.MessageNotification;
import pl.edu.agh.sportsApp.model.notification.Notification;
import pl.edu.agh.sportsApp.notifications.EventType;
import pl.edu.agh.sportsApp.repository.notification.NotificationRepository;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Primary
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationUpdater implements INotificationUpdater {

    @NonNull
    NotificationRepository notificationRepository;

    private Set<Notification> checkValidity(Set<Notification> updatedState) {
        Set<Notification> toDelete = new HashSet<>();
        Iterator<Notification> notificationIterator = updatedState.iterator();

        while(notificationIterator.hasNext()) {
            Notification notification = notificationIterator.next();
            if(notification.getRelatedUsers().size() == 0) {
                toDelete.add(notification);
                if(notification.getEventType().equals(EventType.NEW_MESSAGE))
                    ((MessageNotification)notification).getChat().removeNotification(notification);
                notificationIterator.remove();
            }
        }

        return toDelete;
    }

    @SuppressWarnings("unchecked")
    public void updateNotificationsDb(Set<Notification> updatedState) {
        Set<Notification> toDelete = checkValidity(updatedState);
        notificationRepository.saveAll(updatedState);
        notificationRepository.deleteAll(toDelete);
    }

    public void saveNotificationIfRequired(Notification notification) {
        if(notification.getRelatedUsers().size() > 0)
            notificationRepository.save(notification);
    }

}
