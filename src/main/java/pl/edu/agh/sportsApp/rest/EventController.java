package pl.edu.agh.sportsApp.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.sportsApp.dto.EventDTO;
import pl.edu.agh.sportsApp.dto.EventRequestDTO;
import pl.edu.agh.sportsApp.dto.UserRatingDTO;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.repository.event.projection.RatingFormElement;
import pl.edu.agh.sportsApp.service.EventService;
import pl.edu.agh.sportsApp.service.RatingsService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(description = "Controller for managing events.")
@CrossOrigin
@RestController
@RequestMapping(value = "/events", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class EventController {

    @NonNull
    EventService eventService;
    @NonNull
    RatingsService ratingsService;

    @ApiOperation(value = "Create new event (date format: \"YYYY-MM-DDTHH-mm-ss\"")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Event created."),
            @ApiResponse(code = 401, message = "Log first to gain access.")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@Valid @RequestBody EventRequestDTO eventRequestDTO,
                            @ApiIgnore @AuthenticationPrincipal final User user) {
        eventService.createEvent(eventRequestDTO, user);
    }

    @ApiOperation(value = "Join event with given id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Event joined."),
            @ApiResponse(code = 401, message = "Log first to gain access.")
    })
    @PutMapping("/{id}/add")
    public void addParticipant(@PathVariable("id") Long eventID,
                               @ApiIgnore @AuthenticationPrincipal final User user) {
        eventService.addParticipant(eventID, user.getId());
    }

    @ApiOperation(value = "Leave event with given id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Event leaved."),
            @ApiResponse(code = 400, message = "Wrong event id."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
            @ApiResponse(code = 404, message = "Event not found."),
            @ApiResponse(code = 405, message = "Method not allowed.")
    })
    @PutMapping("/{id}/remove")
    public void removeParticipant(@PathVariable("id") Long eventID,
                                  @ApiIgnore @AuthenticationPrincipal final User user) {
        eventService.removeParticipant(eventID, user.getId());
    }

    @ApiOperation(value = "Get event with given id.", response = EventDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Event returned."),
            @ApiResponse(code = 400, message = "Wrong event id."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
            @ApiResponse(code = 404, message = "Event not found."),
            @ApiResponse(code = 405, message = "Method not allowed.")
    })
    @GetMapping("/{id}")
    public EventDTO getEventById(@PathVariable("id") Long eventID) {
        return eventService.getEvent(eventID).mapToDTO();
    }

    @ApiOperation(value = "Delete event with given id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Event deleted."),
            @ApiResponse(code = 400, message = "Wrong event id."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
            @ApiResponse(code = 403, message = "ResponseCodes = {ACCESS_DENIED}."),
            @ApiResponse(code = 404, message = "Event not found."),
            @ApiResponse(code = 405, message = "Method not allowed.")
    })
    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable("id") Long eventID,
                            @ApiIgnore @AuthenticationPrincipal final User user) {
        eventService.deleteEvent(eventID, user.getId());
    }

    @ApiOperation(value = "Get all events (only for testing)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Events returned."),
            @ApiResponse(code = 401, message = "Log first to gain access.")
    })
    @GetMapping
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    @ApiOperation(value = "Rate participants in finished event.",
            notes = "It is not needed to pass all users from event.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ratings successfully updated."),
            @ApiResponse(code = 400, message = "ResponseCodes = {METHOD_ARGS_NOT_VALID}"),
            @ApiResponse(code = 401, message = "Log first to gain access."),
            @ApiResponse(code = 403, message = "ResponseCodes = {NEED_REQUIRED_RIGHTS, ACCESS_DENIED}. " +
                                                "Access_denied returned when event is current.")
    })
    @PostMapping("/rate/{eventId}/confirm")
    public void rateEventParticipants(@PathVariable("eventId") Long eventId,
                                      @RequestBody List<UserRatingDTO> usersRatings,
                                      @ApiIgnore @AuthenticationPrincipal User evaluativeUser) {
        ratingsService.rateEventParticipants(eventId, usersRatings, evaluativeUser);
    }

    @ApiOperation(value = "Get data required to create participants rating form view on frontend.",
            notes = "If user on list was not rated before then his rating value will be null.",
            response = RatingFormElement.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returned successfully."),
            @ApiResponse(code = 400, message = "Wrong argument type."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
            @ApiResponse(code = 403, message = "ResponseCodes = {NEED_REQUIRED_RIGHTS}")
    })
    @GetMapping("/rate/{eventId}")
    public List<RatingFormElement> getEventRatingFormData(@PathVariable("eventId") Long eventId,
                                                          @ApiIgnore @AuthenticationPrincipal User user) {
        return eventService.getUserRatingFormForEvent(eventId, user.getId());
    }

}
