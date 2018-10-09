package pl.edu.agh.sportsApp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.sportsApp.model.ChatMessage;

import java.util.List;

@Repository
@Transactional
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Integer> {

    List<ChatMessage> getByConversationIdOrderByCreationTime(int conversationId);

}
