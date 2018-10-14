package pl.edu.agh.sportsApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.agh.sportsApp.model.Event;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String location;

    @NotNull
    private LocalDateTime startDate;

    private String content;

    private Long ownerId;
    private List<Long> participantIds;


    public Event parseEvent() {
        return Event.builder()
                .title(this.getTitle())
                .location(this.getLocation())
                .startDate(this.getStartDate())
                .content(this.getContent())
                .build();
    }
}

