package pl.edu.agh.sportsApp.repository.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.agh.sportsApp.model.Message;
import pl.edu.agh.sportsApp.repository.message.projection.ChatMessageData;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> getByChatIdOrderByCreationTime(Long chatId);

    @Query(value = "SELECT m.sender_id senderId, u.first_name firstName, u.last_name lastName, m.content, " +
            "m.creation_time dateTime, p.photo_id senderPhotoId\n" +
            "FROM message m\n" +
            "INNER JOIN event e ON e.chat_id = m.chat_id\n" +
            "INNER JOIN users u ON m.sender_id = u.id\n" +
            "INNER JOIN profile_photo pp on u.id = pp.user_id\n" +
            "INNER JOIN photo p ON pp.id = p.id\n" +
            "WHERE e.id = :eventId\n" +
            "ORDER BY m.creation_time", nativeQuery = true)
    List<ChatMessageData> getEventChatHistoryViewData(@Param("eventId") Long eventId);

    @Query(value = "SELECT m.sender_id senderId, u.first_name firstName, u.last_name lastName, " +
            "m.content, m.creation_time dateTime, p.photo_id senderPhotoId\n" +
            "FROM message m\n" +
            "INNER JOIN users u ON m.sender_id = u.id\n" +
            "INNER JOIN profile_photo pp on u.id = pp.user_id\n" +
            "INNER JOIN photo p ON pp.id = p.id\n" +
            "WHERE m.chat_id = :chatId\n" +
            "ORDER BY m.creation_time", nativeQuery = true)
    List<ChatMessageData> getPrivateChatHistoryViewData(@Param("chatId") Long chatId);

}
