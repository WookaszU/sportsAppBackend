package pl.edu.agh.sportsApp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.sportsApp.dto.EventDTO;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.service.EventService;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping(value = "/events", produces = APPLICATION_JSON_VALUE)
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@Valid @RequestBody EventDTO eventDTO,
                            @AuthenticationPrincipal final User user) {
        eventService.saveEvent(eventDTO, user);
    }

    @PutMapping("/{id}/add")
    public void addParticipant(@PathVariable("id") Long eventID,
                               @AuthenticationPrincipal final User user) {
        eventService.addParticipant(eventID, user.getId());
    }

    @PutMapping("/{id}/remove")
    public void removeParticipant(@PathVariable("id") Long eventID,
                                  @AuthenticationPrincipal final User user) {
        eventService.removeParticipant(eventID, user.getId());
    }

    @GetMapping("/{id}")
    public EventDTO getEventById(@PathVariable("id") Long eventID) {
        return eventService.getEvent(eventID).mapToDTO();
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable("id") Long eventID,
                            @AuthenticationPrincipal final User user) {
        eventService.deleteEvent(eventID, user.getId());
    }
}
