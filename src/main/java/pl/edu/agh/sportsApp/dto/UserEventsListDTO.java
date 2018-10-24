package pl.edu.agh.sportsApp.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import pl.edu.agh.sportsApp.repository.event.projection.EventData;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Data
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class UserEventsListDTO {

    List<EventsListElement> events = new LinkedList<>();

    public UserEventsListDTO(List<EventData> eventDataList) {
        for(EventData eventData : eventDataList)
            this.events.add(EventsListElement.builder()
                    .id(eventData.getId())
                    .title(eventData.getTitle())
                    .startDate(eventData.getStartDate())
                    .content(eventData.getContent())
                    .build());
    }

}

@Builder
@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
class EventsListElement {

    Long id;
    String title;
    LocalDateTime startDate;
    String content;

}
