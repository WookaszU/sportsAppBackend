package pl.edu.agh.sportsApp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.sportsApp.model.Account;
import pl.edu.agh.sportsApp.model.Event;
import pl.edu.agh.sportsApp.service.AccountService;
import pl.edu.agh.sportsApp.service.EventService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("${path.events}")
public class EventRest {
    private static final String INTEGER_REGEX = "\\d{1,9}";

    private final AccountService accountService;
    private final EventService eventService;

    @Autowired
    public EventRest(AccountService accountService, EventService eventService) {
        this.accountService = accountService;
        this.eventService = eventService;
    }



    @CrossOrigin
    @RequestMapping(path = "/{eventId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateEvent(@PathVariable("eventId") String eventId,
                                     @RequestBody Map<String, String> newEvent) {
        if (!eventId.matches(INTEGER_REGEX)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final int id = Integer.parseInt(eventId);
        final Optional<Event> eventOptional = eventService.getEvent(id);
        if (!eventOptional.isPresent()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final String title = newEvent.getOrDefault("title", null);
        final String content = newEvent.getOrDefault("content", null);
        final String userStrId = newEvent.getOrDefault("userId", null);
        if (title == null || content == null || userStrId == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (!userStrId.matches(INTEGER_REGEX)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final int userId = Integer.parseInt(userStrId);
        final Optional<Account> accountOptional = accountService.getAccountById(userId);
        if (!accountOptional.isPresent()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final Account account = accountOptional.get();

        final Event event = eventOptional.get();
        if (account.getEvents().contains(event)) {
            event.setTitle(title);
            event.setContent(content);
            eventService.saveEvent(event);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @CrossOrigin
    @RequestMapping(path = "/{eventId}/{accountId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteEvent(@PathVariable("eventId") String eventId,
                                     @PathVariable("accountId") String accountStrId) {
        if (!eventId.matches(INTEGER_REGEX)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final int id = Integer.parseInt(eventId);
        final Optional<Event> eventOptional = eventService.getEvent(id);
        if (!eventOptional.isPresent()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (!accountStrId.matches(INTEGER_REGEX)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final int userId = Integer.parseInt(accountStrId);
        final Optional<Account> accountOptional = accountService.getAccountById(userId);
        if (!accountOptional.isPresent()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final Account account = accountOptional.get();

        final Event event = eventOptional.get();
        if (account.getEvents().contains(event)) {
            account.getEvents().remove(event);
            eventService.removeEvent(event);
            accountService.saveAccount(account);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
