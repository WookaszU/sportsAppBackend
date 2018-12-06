package pl.edu.agh.sportsApp.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.sportsApp.dto.socket.ChatMessageDTO;
import pl.edu.agh.sportsApp.dto.ResponseCode;
import pl.edu.agh.sportsApp.exceptionHandler.exceptions.NoPermissionsException;
import pl.edu.agh.sportsApp.model.Message;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.notifications.AppEventHandler;
import pl.edu.agh.sportsApp.repository.chat.PrivateChatRepository;
import pl.edu.agh.sportsApp.repository.event.EventRepository;
import pl.edu.agh.sportsApp.repository.message.MessageRepository;
import pl.edu.agh.sportsApp.repository.message.projection.ChatMessageData;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatService {

    @NonNull
    MessageRepository messageRepository;
    @NonNull
    EventRepository eventRepository;
    @NonNull
    PrivateChatRepository privateChatRepository;
    @NonNull
    AppEventHandler appEventHandler;

    @Async
    @Transactional
    public void handleMessageAsyncTasks(final ChatMessageDTO msg, final String chatId,
                                        LocalDateTime localDateTime, final Long userId){

        Message message = messageRepository.save(Message.builder()
                .senderId(userId)
                .chatId(Long.parseLong(chatId))
                .content(msg.getContent())
                .creationTime(localDateTime)
                .build());

        appEventHandler.handleEventChatMessageEvent(Long.parseLong(chatId), userId, localDateTime);
    }

    public List<ChatMessageData> getEventChatHistoryViewData(Long eventId, User user) {
        if(!eventRepository.isUserParticipant(eventId, user.getId()).isPresent())
            throw new NoPermissionsException(ResponseCode.PERMISSION_DENIED.name());
        return messageRepository.getEventChatHistoryViewData(eventId);
    }

    public List<ChatMessageData> getPrivateChatHistoryViewData(Long chatId, User user) {
        if(!privateChatRepository.isUserParticipant(chatId, user.getId()).isPresent())
            throw new NoPermissionsException(ResponseCode.PERMISSION_DENIED.name());
        return messageRepository.getPrivateChatHistoryViewData(chatId);
    }

}
