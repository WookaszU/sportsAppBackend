package pl.edu.agh.sportsApp.notifications.manager;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.model.notification.MessageNotification;
import pl.edu.agh.sportsApp.model.notification.Notification;
import pl.edu.agh.sportsApp.notifications.EventType;
import pl.edu.agh.sportsApp.notifications.manager.sender.INotificationSender;
import pl.edu.agh.sportsApp.notifications.manager.updater.INotificationUpdater;
import pl.edu.agh.sportsApp.repository.user.UserRepository;
import pl.edu.agh.sportsApp.useractivity.UserActivity;

import java.util.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationManager {

    @NonNull
    UserActivity userActivity;
    @NonNull
    UserRepository userRepository;
    @NonNull
    INotificationSender notificationSender;
    @NonNull
    INotificationUpdater notificationUpdater;

    public void manage(Notification notification) {
        List<Long> activeUsersIds = userActivity.filterActiveUsers(notification);

//        activeUsersIds.add(1L); // TEST

        notificationUpdater.saveNotificationIfRequired(notification);
        notificationSender.sendNotificationToUsers(activeUsersIds, notification);
    }

    @Transactional
    public void sendNotificationsToJoiningUser(Long userId) {
        User user  = userRepository.getOne(userId);

        Set<Notification> userNotifications = user.getNotifications();
        removeUserFromNotifications(userNotifications, userId);

        notificationSender.sendStoredNotificationsToUser(userId, groupNotifications(userNotifications));
        notificationUpdater.updateNotificationsDb(userNotifications);
    }


    private void removeUserFromNotifications(Set<Notification> notifications, Long userId) {
        for(Notification notification: notifications)
            notification.removeRelatedUser(userId);
    }

    private Set<Notification> groupNotifications(Set<Notification> beforeGroup) {
        Multimap<String, Notification> aggregatedNotifications = HashMultimap.create();

        for(Notification notification: beforeGroup) {
            if(notification.getEventType().equals(EventType.NEW_MESSAGE)) {
                String key = ((MessageNotification)notification).getChat().getId() + notification.getEventType().name();
                aggregatedNotifications.put(key, notification);
            }
            else
                aggregatedNotifications.put(notification.getId().toString(), notification);
        }

        Set<Notification> groupedNotifications = new HashSet<>();

        for(String key: aggregatedNotifications.keySet())
            groupedNotifications.add(chooseGroupRepresentantive(new LinkedList<>(aggregatedNotifications.get(key))));

        return groupedNotifications;
    }

    private Notification chooseGroupRepresentantive(List<Notification> notificationList){
        Notification choosenOne = notificationList.get(0);

        for(Notification notification: notificationList)
            if(choosenOne.getDateTime().isBefore(notification.getDateTime()))
                choosenOne = notification;

        return choosenOne;
    }

}
