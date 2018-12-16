package pl.edu.agh.sportsApp.model.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.sportsApp.model.Event;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "eventChat")
public class EventChat extends Chat{

    @OneToOne(mappedBy = "eventChat", fetch = FetchType.EAGER)
    private Event event;

}
