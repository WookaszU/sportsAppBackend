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
import java.util.Optional;

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

        Map<Long, User> eventParticipants = event.getParticipants();
        for(Long userId : eventParticipants.keySet()) {
            String userName = eventParticipants.get(userId).getUsername();
            userActivityMap.put(userName, isUserActive(userName));
        }

        return userActivityMap;
    }

    public Optional<User> findUserByToken(String token) {
        return userActivityManager.findByToken(token);
    }

}
