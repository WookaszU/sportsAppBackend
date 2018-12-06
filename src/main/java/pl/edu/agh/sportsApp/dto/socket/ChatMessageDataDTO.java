package pl.edu.agh.sportsApp.dto.socket;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageDataDTO {

    Long senderId;

    String firstName;

    String lastName;

    String content;

    LocalDateTime dateTime;

    String senderPhotoId;

}
