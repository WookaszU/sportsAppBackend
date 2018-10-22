import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.edu.agh.sportsApp.Application;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.model.Event;
import pl.edu.agh.sportsApp.service.UserService;
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
    private UserService userService;

    private final User user = User.builder()
            .email("mail@gmail.com")
            .firstName("firstName")
            .lastName("lastName")
            .build();
    private final String userEmail = "mail@gmail.com";

    @Before
    public void setData() {
        assertNotNull(eventService);
        assertNotNull(userService);
        assertEquals(0, user.getEvents().size());
        assertEquals(false, userService.getUserByEmail(userEmail).isPresent());
        userService.saveUser(user);
        assertEquals(true, userService.getUserByEmail(userEmail).isPresent());
    }

    @Test
    public void saveUserTest() {
        userService.saveUser(user);
        assertEquals(true, userService.getUserByEmail(userEmail).isPresent());
    }

    @Test
    public void addNote() {
        Optional<User> userOptional = userService.getUserByEmail(userEmail);
        assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        assertEquals(0, user.getEvents().size());
        Event event = defaultEvent();
        userService.saveUser(user);
        userOptional = userService.getUserByEmail(userEmail);
        assertTrue(userOptional.isPresent());
        user = userOptional.get();
        assertEquals(1, user.getEvents().size());
    }

    @Test
    public void addManyNotesAndRemove() {
        Optional<User> userOptional = userService.getUserByEmail(userEmail);
        assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        assertEquals(0, user.getEvents().size());
        Event event = defaultEvent();
        event = defaultEvent();
        userService.saveUser(user);

        userOptional = userService.getUserByEmail(userEmail);
        assertTrue(userOptional.isPresent());
        user = userOptional.get();
        assertEquals(2, user.getEvents().size());
        event = user.getEvents().iterator().next();
        user.getEvents().remove(event);
        userService.saveUser(user);

        userOptional = userService.getUserByEmail(userEmail);
        assertTrue(userOptional.isPresent());
        user = userOptional.get();
        assertEquals(1, user.getEvents().size());
    }

    @Test
    public void changeNoteContent() {
        Optional<User> userOptional = userService.getUserByEmail(userEmail);
        assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        assertEquals(0, user.getEvents().size());
        Event event = defaultEvent();
        userService.saveUser(user);

        userOptional = userService.getUserByEmail(userEmail);
        assertTrue(userOptional.isPresent());
        user = userOptional.get();
        assertEquals(1, user.getEvents().size());

        event = user.getEvents().iterator().next();
        assertEquals("content", event.getContent());
        event.setContent("new");
//        eventService.saveEvent(event);

        userOptional = userService.getUserByEmail(userEmail);
        assertTrue(userOptional.isPresent());
        user = userOptional.get();
        assertEquals(1, user.getEvents().size());

        event = user.getEvents().iterator().next();
        assertEquals("new", event.getContent());
    }

    @After
    public void cleanup() {
        user.getEvents().forEach(n -> {
            eventService.removeEvent(n);
        });
        userService.removeUser(user.getId());
    }

    private Event defaultEvent() {
        return Event.builder()
                .title("title")
                .longitude(100D)
                .latitude(100D)
                .content("content")
                .build();
    }
}
