import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.edu.agh.sportsApp.Application;
import pl.edu.agh.sportsApp.model.Account;
import pl.edu.agh.sportsApp.model.Event;
import pl.edu.agh.sportsApp.service.AccountService;
import pl.edu.agh.sportsApp.service.EventService;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
public class Tests2 {
    @Autowired
    private EventService eventService;

    @Autowired
    private AccountService accountService;

    private final Account account = new Account("mail@gmail.com","firstName","lastName");
    private final String accountEmail = "mail@gmail.com";

    @Before
    public void setData() {
        assertNotNull(eventService);
        assertNotNull(accountService);
        assertEquals(0, account.getEvents().size());
        assertEquals(false, accountService.getAccountByEmail(accountEmail).isPresent());
        accountService.saveAccount(account);
        assertEquals(true, accountService.getAccountByEmail(accountEmail).isPresent());
    }

    @Test
    public void saveUserTest() {
        accountService.saveAccount(account);
        assertEquals(true, accountService.getAccountByEmail(accountEmail).isPresent());
    }

    @Test
    public void addNote() {
        Optional<Account> accountOptional = accountService.getAccountByEmail(accountEmail);
        assertTrue(accountOptional.isPresent());
        Account account = accountOptional.get();
        assertEquals(0, account.getEvents().size());
        Event event = defaultEvent();
        account.addEvent(event);
        accountService.saveAccount(account);
        accountOptional = accountService.getAccountByEmail(accountEmail);
        assertTrue(accountOptional.isPresent());
        account = accountOptional.get();
        assertEquals(1, account.getEvents().size());
    }

    @Test
    public void addManyNotesAndRemove() {
        Optional<Account> accountOptional = accountService.getAccountByEmail(accountEmail);
        assertTrue(accountOptional.isPresent());
        Account account = accountOptional.get();
        assertEquals(0, account.getEvents().size());
        Event event = defaultEvent();
        account.addEvent(event);
        event = defaultEvent();
        account.addEvent(event);
        accountService.saveAccount(account);

        accountOptional = accountService.getAccountByEmail(accountEmail);
        assertTrue(accountOptional.isPresent());
        account = accountOptional.get();
        assertEquals(2, account.getEvents().size());
        event = account.getEvents().get(0);
        account.getEvents().remove(event);
        accountService.saveAccount(account);

        accountOptional = accountService.getAccountByEmail(accountEmail);
        assertTrue(accountOptional.isPresent());
        account = accountOptional.get();
        assertEquals(1, account.getEvents().size());
    }

    @Test
    public void changeNoteContent() {
        Optional<Account> accountOptional = accountService.getAccountByEmail(accountEmail);
        assertTrue(accountOptional.isPresent());
        Account account = accountOptional.get();
        assertEquals(0, account.getEvents().size());
        Event event = defaultEvent();
        account.addEvent(event);
        accountService.saveAccount(account);

        accountOptional = accountService.getAccountByEmail(accountEmail);
        assertTrue(accountOptional.isPresent());
        account = accountOptional.get();
        assertEquals(1, account.getEvents().size());

        event = account.getEvents().get(0);
        assertEquals("content", event.getContent());
        event.setContent("new");
        eventService.saveEvent(event);

        accountOptional = accountService.getAccountByEmail(accountEmail);
        assertTrue(accountOptional.isPresent());
        account = accountOptional.get();
        assertEquals(1, account.getEvents().size());

        event = account.getEvents().get(0);
        assertEquals("new", event.getContent());
    }

    @After
    public void cleanup() {
        account.getEvents().forEach(n -> {
            eventService.removeEvent(n);
        });
        accountService.removeAccount(account.getId());
    }

    private Event defaultEvent() {
        return new Event("title","0:0:0", "content");
    }
}
