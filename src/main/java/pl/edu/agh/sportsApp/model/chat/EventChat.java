package pl.edu.agh.sportsApp.model.chat;

import lombok.Data;
import pl.edu.agh.sportsApp.model.Event;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Data
@Entity(name = "eventChat")
public class EventChat extends Chat{

    @OneToOne(mappedBy = "eventChat", fetch = FetchType.EAGER)
    private Event event;

}
