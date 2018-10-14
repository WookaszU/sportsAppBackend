package pl.edu.agh.sportsApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.sportsApp.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

}
