package pl.edu.agh.sportsApp.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@NoArgsConstructor
@Entity(name = "chat_message")
@Data
public class ChatMessage {

    @Column
    @Id
    @GeneratedValue
    private int id;

    @Column
    private int senderId;

    @Column
    private int conversationId;

    @Column
    private ZonedDateTime creationTime;

    @Column
    private String content;

    @Builder
    public ChatMessage(int senderId, int conversationId, ZonedDateTime creationTime, String content) {
        this.senderId = senderId;
        this.conversationId = conversationId;
        this.creationTime = creationTime;
        this.content = content;
    }

}
