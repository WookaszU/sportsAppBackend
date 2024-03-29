package pl.edu.agh.sportsApp.notifications;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import pl.edu.agh.sportsApp.notifications.creators.RestNotificationCreator;
import pl.edu.agh.sportsApp.notifications.creators.SocketNotificationCreator;
import pl.edu.agh.sportsApp.notifications.manager.NotificationManager;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class AppEventHandler {

    @NonNull
    RestNotificationCreator restNotificationCreator;
    @NonNull
    SocketNotificationCreator socketNotificationCreator;
    @NonNull
    NotificationManager notificationManager;

    public void handleEventChatMessageEvent(Long chatId, Long userId, LocalDateTime dateTime) {
        notificationManager.manage(socketNotificationCreator.newEventChatMessage(chatId, userId, dateTime));
    }

    public void handlePrivateChatMessageEvent(Long chatId, Long userId, LocalDateTime dateTime) {
        notificationManager.manage(socketNotificationCreator.newPrivateChatMessage(chatId, userId, dateTime));
    }

    public void handleUserLoginEvent(Long userId) {
        notificationManager.sendNotificationsToJoiningUser(userId);
    }

}
