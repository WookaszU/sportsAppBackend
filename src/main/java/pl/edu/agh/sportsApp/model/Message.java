package pl.edu.agh.sportsApp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Entity(name = "message")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Column
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long senderId;

    @Column
    private Long conversationId;

    @Column
    private ZonedDateTime creationTime;

    @Column
    private String content;

}
