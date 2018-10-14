package pl.edu.agh.sportsApp.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.model.ChatMessage;
import pl.edu.agh.sportsApp.repository.ChatMessageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageStorage {

    @NonNull
    ChatMessageRepository chatMessageRepository;

    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    // TODO CZY TUTAJ TO SORTOWAC???   niby jest pobierane dobrze w kolejnosci dodania do bazy chyba
    public List<ChatMessage> getMessagesByConversationId(Long conversationId) {
        return chatMessageRepository.getByConversationIdOrderByCreationTime(conversationId);
    }

}
