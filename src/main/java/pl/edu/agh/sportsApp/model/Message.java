package pl.edu.agh.sportsApp.model;

import lombok.*;
import pl.edu.agh.sportsApp.model.chat.Chat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "message")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"chat"})
public class Message {

    @Column
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long senderId;

    @ManyToOne
    @JoinColumn(name = "chatId", referencedColumnName = "id")
    private Chat chat;

    @Column
    private LocalDateTime creationTime;

    @Column
    private String content;

}
