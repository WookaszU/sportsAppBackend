package pl.edu.agh.sportsApp.repository.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.agh.sportsApp.model.Event;
import pl.edu.agh.sportsApp.repository.event.projection.EventData;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT E.ID, E.CONTENT," +
            "E.OWNER_ID AS ownerId, E.START_DATE AS startDate, E.CATEGORY_ID AS categoryId\n" +
            "FROM EVENT as E\n" +
            "INNER JOIN USER_EVENTS AS UE ON E.ID = UE.EVENT_ID\n" +
            "WHERE UE.USERS_ID = :userId AND E.START_DATE > NOW() + INTERVAL '30 minutes'", nativeQuery = true)
    List<EventData> getActualUserEvents(@Param("userId") Long userId);

    @Query(value = "SELECT E.ID, E.CONTENT," +
            "E.START_DATE AS startDate, E.CATEGORY_ID AS categoryId\n" +
            "FROM EVENT as E\n" +
            "INNER JOIN USER_EVENTS AS UE ON E.ID = UE.EVENT_ID\n" +
            "WHERE UE.USERS_ID = :userId AND E.START_DATE < NOW() + INTERVAL '30 minutes'", nativeQuery = true)
    List<EventData> getArchivedUserEvents(@Param("userId") Long userId);

    List<Event> getALlByStartDateIsBetween(LocalDateTime startDate, LocalDateTime endDate);
}
