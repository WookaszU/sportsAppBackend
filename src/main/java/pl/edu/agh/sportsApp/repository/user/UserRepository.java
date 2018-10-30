package pl.edu.agh.sportsApp.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.agh.sportsApp.model.User;
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

}
