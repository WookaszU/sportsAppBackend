package pl.edu.agh.sportsApp.notifications.creators;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.sportsApp.model.Message;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.model.chat.EventChat;
import pl.edu.agh.sportsApp.model.chat.PrivateChat;
import pl.edu.agh.sportsApp.model.notification.MessageNotification;
import pl.edu.agh.sportsApp.model.notification.Notification;
import pl.edu.agh.sportsApp.repository.chat.EventChatRepository;
import pl.edu.agh.sportsApp.repository.chat.PrivateChatRepository;
import pl.edu.agh.sportsApp.repository.notification.NotificationRepository;

import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class SocketNotificationCreator {

    @NonNull
    EventChatRepository eventChatRepository;
    @NonNull
    PrivateChatRepository privateChatRepository;
    @NonNull
    NotificationRepository notificationRepository;

    @Transactional
    public Notification newEventChatMessage(Message newMessage) {
        EventChat chat = eventChatRepository.getOne(newMessage.getChatId());

        MessageNotification notification = MessageNotification.builder()
                .chat(chat)
                .dateTime(newMessage.getCreationTime())
                .build();

        for(User user: chat.getEvent().getParticipants().values())
            if(!user.getId().equals(newMessage.getSenderId())) {
                notification.addRelatedUser(user);
                user.addNotification(notification);
            }

        chat.addNotification(notification);
        return notification;
    }

    @Transactional
    public Notification newPrivateChatMessage(Message newMessage) {

        PrivateChat chat = privateChatRepository.getOne(newMessage.getChatId());
        Map<Long, User> relatedUsers = chat.getParticipants();
        relatedUsers.remove(newMessage.getSenderId());

        Notification notification = MessageNotification.builder()
                .chat(chat)
                .dateTime(newMessage.getCreationTime())
                .build();

        for(User user: chat.getParticipants().values())
            if(!user.getId().equals(newMessage.getSenderId())) {
                notification.addRelatedUser(user);
                user.addNotification(notification);
            }

        return notification;
    }

}
