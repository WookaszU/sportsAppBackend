package pl.edu.agh.sportsApp.repository.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.agh.sportsApp.model.Event;
import pl.edu.agh.sportsApp.repository.event.projection.EventData;
import pl.edu.agh.sportsApp.repository.event.projection.RatingFormElement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Query(value = "SELECT users_id userId, U.first_name firstName, U.last_name lastName, P.photo_id photoId, UR.rating rating\n" +
            "FROM user_events  UE\n" +
            "INNER JOIN users  U ON UE.users_id = U.id\n" +
            "LEFT JOIN profile_photo PP ON U.id = PP.user_id\n" +
            "LEFT JOIN photo P ON PP.id = P.id\n" +
            "LEFT JOIN user_rating UR ON U.id = UR.rated_user_id AND UR.evaluative_user_id = :userId\n" +
            "WHERE UE.event_id = :eventId", nativeQuery = true)
    List<RatingFormElement> getUserRatingFormForEvent(@Param("eventId") Long eventId, @Param("userId") Long userId);

    @Query(value = "SELECT ue.users_id\n" +
            "FROM event e\n" +
            "INNER JOIN user_events ue ON e.id = ue.event_id\n" +
            "WHERE ue.users_id = :userId AND e.id = :eventId", nativeQuery = true)
    Optional<Long> isUserParticipant(@Param("eventId") Long eventId, @Param("userId") Long userId);

    List<Event> getALlByStartDateIsBetween(LocalDateTime startDate, LocalDateTime endDate);
}
