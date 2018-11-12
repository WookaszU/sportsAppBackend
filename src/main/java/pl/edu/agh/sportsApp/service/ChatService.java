package pl.edu.agh.sportsApp.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.dateservice.DateService;
import pl.edu.agh.sportsApp.dto.ChatMessageDTO;
import pl.edu.agh.sportsApp.model.Message;
import pl.edu.agh.sportsApp.model.User;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatService {

    @NonNull
    DateService dateService;
    @NonNull
    ChatMessageStorage messageStorage;

    @Async
    public void handleMessageAsyncTasks(final ChatMessageDTO msg, final String chatId, final Long userId){

        Message message = messageStorage.save(Message.builder()
                .senderId(userId)
                .conversationId(Long.parseLong(chatId))
                .content(msg.getContent())
                .creationTime(dateService.now())
                .build());

        // notifications here todo
    }
}
