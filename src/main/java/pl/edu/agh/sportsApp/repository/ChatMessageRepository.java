package pl.edu.agh.sportsApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.sportsApp.model.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> getByConversationIdOrderByCreationTime(Long conversationId);

}
