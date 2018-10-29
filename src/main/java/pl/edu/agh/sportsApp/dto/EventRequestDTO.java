package pl.edu.agh.sportsApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import pl.edu.agh.sportsApp.model.Event;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDTO {
    @NotNull
    private int categoryId;

    @NotNull
    @Latitude
    private Double latitude;

    @NotNull
    @Longitude
    private Double longitude;

    @NotNull
    private LocalDateTime startDate;

    private String content;

    public Event parseEvent() {
        return Event.builder()
                .categoryId(this.getCategoryId())
                .startDate(this.getStartDate())
                .latitude(this.getLatitude())
                .longitude(this.getLongitude())
                .content(this.getContent())
                .build();
    }
}
