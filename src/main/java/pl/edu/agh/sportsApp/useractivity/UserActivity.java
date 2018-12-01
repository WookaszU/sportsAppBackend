package pl.edu.agh.sportsApp.useractivity;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import pl.edu.agh.sportsApp.model.Event;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.model.notification.Notification;
import pl.edu.agh.sportsApp.service.EventService;
import pl.edu.agh.sportsApp.useractivity.manager.UserActivityManager;

import java.util.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserActivity {

    UserActivityManager userActivityManager;
    EventService eventService;

    private boolean isUserActive(String userName) {
        return userActivityManager.getActiveUsers().containsKey(userName);
    }

    public Map<Long, Boolean> getEventParticipantsActivities(Long eventId) {
        Event event = eventService.getEvent(eventId);
        Map<Long, Boolean> userActivityMap = new HashMap<>();

        Map<Long, User> eventParticipants = event.getParticipants();
        for(Long userId : eventParticipants.keySet()) {
            String userName = eventParticipants.get(userId).getUsername();
            userActivityMap.put(userId, isUserActive(userName));
        }

        return userActivityMap;
    }

    public Map<Long, Boolean> getUsersActivities(List<User> users) {
        Map<Long, Boolean> userActivityMap = new HashMap<>();

        for(User user: users)
            userActivityMap.put(user.getId(), isUserActive(user.getUsername()));

        return userActivityMap;
    }

    public List<Long> filterActiveUsers(Notification notification) {
        List<Long> activeUsers = new LinkedList<>();
        Iterator<Map.Entry<Long, Boolean>> itr = getUsersActivities(
                new LinkedList<>(notification.getRelatedUsers().values())).entrySet().iterator();

        while(itr.hasNext()) {
            Map.Entry<Long, Boolean> entry = itr.next();
            if(entry.getValue()) {
                activeUsers.add(entry.getKey());
                notification.getRelatedUsers().remove(entry.getKey());
            }
        }

        return activeUsers;
    }

    public Optional<User> findUserByToken(String token) {
        return userActivityManager.findByToken(token);
    }

}
