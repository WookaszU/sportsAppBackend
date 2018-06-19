package pl.edu.agh.sportsApp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.sportsApp.model.Event;

import java.util.List;

@Transactional
public interface EventRepository extends CrudRepository<Event, Integer> {
    Event findById(Integer eventId);

    void removeEventById(Integer eventId);

    List<Event> findAll();
}
