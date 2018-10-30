package pl.edu.agh.sportsApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.agh.sportsApp.model.UserRating;

import java.util.List;
import java.util.Optional;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {

    List<UserRating> findByRatedUserIdAndEventId(Long ratedUserId, Long eventId);

    @Query(value = "SELECT AVG(rating) FROM USER_RATING WHERE RATED_USER_ID = :userId", nativeQuery = true)
    Double getUserAvgRating(@Param(value = "userId") Long userId);

    Optional<UserRating> findByRatedUserIdAndEventIdAndEvaluativeUserId(Long ratedUserId,
                                                                        Long eventId,
                                                                        Long evaluativeUserId);

}
