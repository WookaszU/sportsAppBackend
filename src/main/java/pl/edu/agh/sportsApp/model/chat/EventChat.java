package pl.edu.agh.sportsApp.model.chat;

import lombok.*;
import pl.edu.agh.sportsApp.model.Event;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Getter @Setter
@AllArgsConstructor
@Entity(name = "eventChat")
public class EventChat extends Chat{

    @OneToOne(mappedBy = "eventChat", fetch = FetchType.EAGER)
    private Event event;

}
