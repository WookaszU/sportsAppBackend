package pl.edu.agh.sportsApp.model.photo;

import lombok.*;
import pl.edu.agh.sportsApp.model.Event;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "eventPhoto")
@Getter @Setter
public class EventPhoto extends Photo {

    @ManyToOne
    @JoinColumn(name = "eventId", referencedColumnName = "id", nullable = false)
    private Event event;

    public EventPhoto(String photoId, String highResolutionPath, String lowResolutionPath) {
        super(photoId, highResolutionPath, lowResolutionPath);
    }
}
