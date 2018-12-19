package pl.edu.agh.sportsApp.notifications.creators;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.model.chat.EventChat;
import pl.edu.agh.sportsApp.model.chat.PrivateChat;
import pl.edu.agh.sportsApp.model.notification.MessageNotification;
import pl.edu.agh.sportsApp.model.notification.Notification;
import pl.edu.agh.sportsApp.repository.chat.EventChatRepository;
import pl.edu.agh.sportsApp.repository.chat.PrivateChatRepository;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class SocketNotificationCreator {

    @NonNull
    EventChatRepository eventChatRepository;
    @NonNull
    PrivateChatRepository privateChatRepository;

    @SuppressWarnings({"OptionalGetWithoutIsPresent"})
    public Notification newEventChatMessage(Long chatId, Long userId, LocalDateTime dateTime) {
        EventChat chat = eventChatRepository.findById(chatId).get();

        MessageNotification notification = MessageNotification.builder()
                .chat(chat)
                .dateTime(dateTime)
                .build();

        for(User user: chat.getEvent().getParticipants().values())
            if(!user.getId().equals(userId)) {
                notification.addRelatedUser(user);
                user.addNotification(notification);
            }

        chat.addNotification(notification);
        return notification;
    }

    @SuppressWarnings({"OptionalGetWithoutIsPresent"})
    public Notification newPrivateChatMessage(Long chatId, Long userId, LocalDateTime dateTime) {

        PrivateChat chat = privateChatRepository.findById(chatId).get();
        Map<Long, User> relatedUsers = chat.getParticipants();
        relatedUsers.remove(userId);

        Notification notification = MessageNotification.builder()
                .chat(chat)
                .dateTime(dateTime)
                .build();

        for(User user: chat.getParticipants().values())
            if(!user.getId().equals(userId)) {
                notification.addRelatedUser(user);
                user.addNotification(notification);
            }

        return notification;
    }

}
