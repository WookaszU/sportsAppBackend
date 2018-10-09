package pl.edu.agh.sportsApp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.sportsApp.model.Event;

import java.util.List;

@Repository
@Transactional
public interface EventRepository extends CrudRepository<Event, Integer> {

    void removeEventById(Integer eventId);

    List<Event> findAll();
}
