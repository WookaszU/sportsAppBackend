package pl.edu.agh.sportsApp.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.sportsApp.dto.EventDTO;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.service.EventService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(description = "Controller for managing events.")
@CrossOrigin
@RestController
@RequestMapping(value = "/events", produces = APPLICATION_JSON_VALUE)
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @ApiOperation(value = "Create new event.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Event created."),
            @ApiResponse(code = 401, message = "Log first to gain access.")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@Valid @RequestBody EventDTO eventDTO,
                            @ApiIgnore @AuthenticationPrincipal final User user) {
        eventService.saveEvent(eventDTO, user);
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
}