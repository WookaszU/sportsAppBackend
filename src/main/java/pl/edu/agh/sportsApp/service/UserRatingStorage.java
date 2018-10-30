package pl.edu.agh.sportsApp.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.model.UserRating;
import pl.edu.agh.sportsApp.repository.UserRatingRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRatingStorage {

    UserRatingRepository ratingRepository;

    public Optional<UserRating> findUserRatingById(Long ratingId) {
        return ratingRepository.findById(ratingId);
    }

    public Optional<UserRating> findUserRatingByDtoArgs(Long ratedUserId, Long eventId, Long evaluativeUserId) {
        return ratingRepository.findByRatedUserIdAndEventIdAndEvaluativeUserId(ratedUserId, eventId, evaluativeUserId);
    }

    public void save(UserRating userRating) {
        ratingRepository.save(userRating);
    }

    public void remove(Long id) {
        ratingRepository.deleteById(id);
    }

    public void remove(UserRating userRating) {
        ratingRepository.delete(userRating);
    }

    public List<UserRating> getUserRatingsByEvent(Long userId, Long eventId) {
        return ratingRepository.findByRatedUserIdAndEventId(userId, eventId);
    }

    public Double getUserAvgRating(Long userId) {
        return ratingRepository.getUserAvgRating(userId);
    }

}
