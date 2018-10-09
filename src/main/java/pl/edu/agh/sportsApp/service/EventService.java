package pl.edu.agh.sportsApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.model.Event;
import pl.edu.agh.sportsApp.repository.EventRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository){
        this.eventRepository=eventRepository;
    }

    public void saveEvent(Event event){eventRepository.save(event);}

    public List<Event> getEvents(){return eventRepository.findAll();}

    public Optional<Event> getEvent(int id) {
        return eventRepository.findById(id);
    }

    public void removeEvent(Event event) {
        eventRepository.removeEventById(event.getId());
    }
}
