package pl.edu.agh.sportsApp.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.repository.user.projection.UserData;
import pl.edu.agh.sportsApp.repository.user.projection.UserRatingData;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query(value = "SELECT UR.ID, UR.DESCRIPTION, UR.RATING, UR.EVENT_ID AS eventId, " +
            "UR.EVALUATIVE_USER_ID AS evaluativeId, " +
            "U.FIRST_NAME AS evaluativeFirstName, U.LAST_NAME AS evaluativeLastName\n" +
            "FROM USER_RATING UR\n" +
            "INNER JOIN USERS U ON U.ID = UR.EVALUATIVE_USER_ID\n" +
            "WHERE UR.RATED_USER_ID = :userId", nativeQuery = true)
    List<UserRatingData> getUserRatingsWithEvaluativeNames(@Param(value = "userId") Long userId);

    @Query(value = "SELECT u.id, u.first_name firstName, u.last_name lastName, u.rating,\n" +
            "       (SELECT photo_id\n" +
            "       FROM photo p\n" +
            "       INNER JOIN profile_photo pp ON p.id = pp.id\n" +
            "       WHERE pp.user_id = u.id) photoId,\n" +
            "       (SELECT e.category_id\n" +
            "        FROM user_events ue\n" +
            "        INNER JOIN event e ON ue.event_id = e.id\n" +
            "        WHERE ue.users_id = u.id AND e.start_date < now()\n" +
            "        GROUP BY e.category_id\n" +
            "        ORDER BY COUNT(*) DESC\n" +
            "        LIMIT 1)  favoriteCategory,\n" +
            "       (SELECT COUNT(*)\n" +
            "        FROM user_events ue\n" +
            "        INNER JOIN event e ON ue.event_id = e.id\n" +
            "        WHERE ue.users_id = u.id AND e.start_date < now())  eventParticipationNum,\n" +
            "       (SELECT COUNT(*)\n" +
            "        FROM event\n" +
            "        WHERE owner_id = u.id AND start_date < now())  eventOwnershipsNum,\n" +
            "       (SELECT COUNT(*)\n" +
            "        FROM user_rating\n" +
            "        WHERE rated_user_id = u.id) userRatingsNum\n" +
            "FROM users u\n" +
            "WHERE u.id = :userId", nativeQuery = true)
    UserData getUserStatistics(@Param(value = "userId") Long userId);

}
