package pl.edu.agh.sportsApp.repository.chat;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.agh.sportsApp.model.chat.PrivateChat;

import java.util.Optional;

public interface PrivateChatRepository extends ChatRepository<PrivateChat> {

    @Query(value = "SELECT u.user_id\n" +
            "FROM chat c\n" +
            "INNER JOIN user_chats u on c.id = u.chat_id\n" +
            "WHERE c.id = :chatId AND u.user_id = :userId", nativeQuery = true)
    Optional<Long> isUserParticipant(@Param("chatId") Long chatId, @Param("userId") Long userId);

}
