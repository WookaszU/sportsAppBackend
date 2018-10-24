package pl.edu.agh.sportsApp.useractivity;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import pl.edu.agh.sportsApp.model.Event;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.service.EventService;
import pl.edu.agh.sportsApp.useractivity.manager.UserActivityManager;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserActivity {

    UserActivityManager userActivityManager;
    EventService eventService;

    private boolean isUserActive(String userName) {
        return userActivityManager.getActiveUsers().containsKey(userName);
    }

    public Map<String, Boolean> getEventParticipantsActivities(Long eventId) {
        Event event = eventService.getEvent(eventId);
        Map<String, Boolean> userActivityMap = new HashMap<>();

        for(User user : event.getParticipants()) {
            userActivityMap.put(user.getUsername(), isUserActive(user.getUsername()));
        }

        return userActivityMap;
    }



}
