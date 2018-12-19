package pl.edu.agh.sportsApp.model;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
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
    @Cascade(CascadeType.SAVE_UPDATE)
    private Chat chat;

    @Column
    private LocalDateTime creationTime;

    @Column
    private String content;

}
