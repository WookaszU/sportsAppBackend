package pl.edu.agh.sportsApp.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.dto.ResponseCode;
import pl.edu.agh.sportsApp.dto.UserRatingDTO;
import pl.edu.agh.sportsApp.exceptionHandler.exceptions.NoPermissionsException;
import pl.edu.agh.sportsApp.exceptionHandler.exceptions.ValidationException;
import pl.edu.agh.sportsApp.model.Event;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.model.UserRating;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingsService {

    UserService userService;
    UserRatingStorage ratingStorage;
    EventService eventService;

    public void handleRatingEvent(UserRatingDTO ratingDTO, User user) {
        if(ratingDTO.getUserId().equals(user.getId()))
            throw new ValidationException(ResponseCode.METHOD_ARGS_NOT_VALID.name());

        Optional<UserRating> userRatingOpt = ratingStorage.findUserRatingByDtoArgs(
                ratingDTO.getUserId(), ratingDTO.getEventId(), user.getId());
        if(userRatingOpt.isPresent())
            updateUserRating(ratingDTO, userRatingOpt.get(), user);
        else
            createUserRating(ratingDTO, user);

        updateAvgRating(ratingDTO.getUserId());
    }

    // TODO change it to TRIGGER, code for trigger in import.sql >> h2 db not support sql triggers > mysql
    private void updateAvgRating(Long userId) {
        User user = userService.getUserById(userId);
        user.setRating(ratingStorage.getUserAvgRating(userId));
        userService.saveUser(user);
    }

    private Event validRatingCreateArgs(UserRatingDTO userRatingDTO, User user) {
        if(userRatingDTO.getRating() == null)
            throw new ValidationException(ResponseCode.METHOD_ARGS_NOT_VALID.name());

        Optional<Event> eventOpt = eventService.findEventById(userRatingDTO.getEventId());
        if(!eventOpt.isPresent())
            throw new EntityNotFoundException(ResponseCode.METHOD_ARGS_NOT_VALID.name());

        Event event = eventOpt.get();
        if(!event.getParticipants().containsKey(user.getId()))
            throw new NoPermissionsException(ResponseCode.NEED_REQUIRED_RIGHTS.name());

        if(!event.getParticipants().containsKey(userRatingDTO.getUserId()))
            throw new ValidationException(ResponseCode.METHOD_ARGS_NOT_VALID.name());

        return event;
    }

    private void createUserRating(UserRatingDTO userRatingDTO, User user) {
        Event event = validRatingCreateArgs(userRatingDTO, user);

        UserRating userRating = UserRating.builder()
                .rating(userRatingDTO.getRating().doubleValue())
                .evaluativeUserId(user.getId())
                .ratedUser(event.getParticipants().get(userRatingDTO.getUserId()))
                .event(event)
                .description(userRatingDTO.getDescription() != null ? userRatingDTO.getDescription() : "")
                .build();

        userRating.getEvent().addRating(userRating);
        userRating.getRatedUser().addUserRating(userRating);

        ratingStorage.save(userRating);
    }

    private void validRatingUpdateArgs(UserRating userRating, User user) {
        if(!user.getId().equals(userRating.getEvaluativeUserId()))
            throw new NoPermissionsException(ResponseCode.NEED_REQUIRED_RIGHTS.name());
    }

    private void updateUserRating(UserRatingDTO userRatingUpdate, UserRating userRating, User user) {
        validRatingUpdateArgs(userRating, user);

        if(userRatingUpdate.getRating() != null)
            userRating.setRating(userRatingUpdate.getRating().doubleValue());

        if(userRatingUpdate.getDescription() != null)
            userRating.setDescription(userRatingUpdate.getDescription());

        ratingStorage.save(userRating);
    }

}
