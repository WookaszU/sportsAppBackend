package pl.edu.agh.sportsApp.repository.event.projection;

import java.time.LocalDateTime;

public interface EventData {

    Long getId();

    String getContent();

    LocalDateTime getStartDate();

    String getTitle();

}
