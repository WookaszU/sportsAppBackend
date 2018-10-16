package pl.edu.agh.sportsApp.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.model.Message;
import pl.edu.agh.sportsApp.repository.MessageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageStorage {

    @NonNull
    MessageRepository messageRepository;

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    // TODO CZY TUTAJ TO SORTOWAC???   niby jest pobierane dobrze w kolejnosci dodania do bazy chyba
    public List<Message> getMessagesByConversationId(Long conversationId) {
        return messageRepository.getByConversationIdOrderByCreationTime(conversationId);
    }

}
