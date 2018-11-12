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
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingsService {

    UserRatingStorage ratingStorage;
    EventService eventService;

    public void rateEventParticipants(Long eventId, List<UserRatingDTO> usersRatings, User evaluativeUser) {
        checkRatingsCorrectness(usersRatings, evaluativeUser.getId());
        List<UserRating> ratingsToSave = new LinkedList<>();

        for(UserRatingDTO rating: usersRatings)
            ratingsToSave.add(rateSingleParticipant(rating, eventId, evaluativeUser));

        ratingStorage.saveAll(ratingsToSave);
    }

    private UserRating rateSingleParticipant(UserRatingDTO ratingDTO, Long eventId, User evaluativeUser) {
        Optional<UserRating> userRatingOpt = ratingStorage.findUserRatingByDtoArgs(
                ratingDTO.getUserId(), eventId, evaluativeUser.getId());

        return userRatingOpt.isPresent() ?
                updateUserRating(ratingDTO, userRatingOpt.get()) :
                createUserRating(ratingDTO, eventId, evaluativeUser);
    }

    private void checkRatingsCorrectness(List<UserRatingDTO> usersRatings, Long evaluativeUserId) {
        for(UserRatingDTO rating: usersRatings)
            if(rating.getUserId().equals(evaluativeUserId) ||
                    rating.getRating() == null ||
                    rating.getRating() > 5 || rating.getRating() < 1)
                throw new ValidationException(ResponseCode.METHOD_ARGS_NOT_VALID.name());
    }

    private Event checkRatingCreateArguments(UserRatingDTO userRatingDTO, Long eventId, User evaluativeUser) {
        Optional<Event> eventOpt = eventService.findEventById(eventId);
        if(!eventOpt.isPresent())
            throw new EntityNotFoundException(ResponseCode.METHOD_ARGS_NOT_VALID.name());

        Event event = eventOpt.get();
        if(event.getStartDate().isAfter(LocalDateTime.now()))
            throw new NoPermissionsException(ResponseCode.ACCESS_DENIED.name());

        if(!event.getParticipants().containsKey(evaluativeUser.getId()))
            throw new NoPermissionsException(ResponseCode.NEED_REQUIRED_RIGHTS.name());

        if(!event.getParticipants().containsKey(userRatingDTO.getUserId()))
            throw new ValidationException(ResponseCode.METHOD_ARGS_NOT_VALID.name());

        return event;
    }

    private UserRating createUserRating(UserRatingDTO userRatingDTO,Long eventId, User evaluativeUser) {
        Event event = checkRatingCreateArguments(userRatingDTO, eventId, evaluativeUser);

        UserRating userRating = UserRating.builder()
                .rating(userRatingDTO.getRating().doubleValue())
                .evaluativeUserId(evaluativeUser.getId())
                .ratedUser(event.getParticipants().get(userRatingDTO.getUserId()))
                .event(event)
                .build();

        userRating.getEvent().addRating(userRating);
        userRating.getRatedUser().addUserRating(userRating);

        return userRating;
    }

    private UserRating updateUserRating(UserRatingDTO userRatingUpdate, UserRating userRating) {
        if(userRatingUpdate.getRating() != null)
            userRating.setRating(userRatingUpdate.getRating().doubleValue());

        return userRating;
    }

}
