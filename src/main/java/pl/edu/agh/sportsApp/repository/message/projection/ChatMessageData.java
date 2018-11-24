package pl.edu.agh.sportsApp.repository.message.projection;

import java.time.LocalDateTime;

public interface ChatMessageData {

    Long getSenderId();

    String getFirstName();

    String getLastName();

    String getContent();

    LocalDateTime getDateTime();

    Long getSenderPhotoId();

}
