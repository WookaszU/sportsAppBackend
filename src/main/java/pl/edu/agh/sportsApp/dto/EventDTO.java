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
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Long id;

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

    private Long ownerId;
    private List<Long> participantIds;
}

