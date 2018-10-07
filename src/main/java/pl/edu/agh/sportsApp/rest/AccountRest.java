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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("${path.accounts}")
public class AccountRest {

    private static final String INTEGER_REGEX = "\\d{1,9}";

    private final AccountService accountService;
    private final EventService eventService;

    @Autowired
    public AccountRest(AccountService accountService, EventService eventService) {
        this.accountService = accountService;
        this.eventService = eventService;
    }

    @CrossOrigin
    @RequestMapping(path = "/addaccount", method = RequestMethod.POST)
    public ResponseEntity addAccount(@RequestBody Map<String, String> newAccount) {
        final String email = newAccount.getOrDefault("email", null);
        final String firstName = newAccount.getOrDefault("firstName",null);
        final String lastName = newAccount.getOrDefault("lastName", null);
        if (email == null || lastName == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final Account account = new Account(email,firstName,lastName);
        accountService.saveAccount(account);
        return new ResponseEntity(HttpStatus.OK);
    }


    @CrossOrigin
    @RequestMapping(path = "/{accountId}/events", method = RequestMethod.GET)
    public ResponseEntity<List<Event>> getAccountEvents(@PathVariable("accountId") String accountId) {
        if (!accountId.matches(INTEGER_REGEX)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final int id = Integer.parseInt(accountId);
        final Optional<Account> accountOptional = accountService.getAccountById(id);
        if (!accountOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Account account = accountOptional.get();
        return new ResponseEntity<>(account.getEvents(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "/{accountId}/events", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addEventToAccount(@PathVariable("accountId") String accountId, @RequestBody Map<String, String> newEvent) {
        if (!accountId.matches(INTEGER_REGEX)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final int id = Integer.parseInt(accountId);
        final Optional<Account> accountOptional = accountService.getAccountById(id);
        if (!accountOptional.isPresent()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final Account account = accountOptional.get();

        final String title = newEvent.getOrDefault("title", null);
        final String location = newEvent.getOrDefault("location",null);
        final String content = newEvent.getOrDefault("content", null);
        if (title == null || content == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        final Event event = new Event(title,location, content);
        eventService.saveEvent(event);
        account.addEvent(event);
        accountService.saveAccount(account);
        return new ResponseEntity(HttpStatus.OK);
    }
}
