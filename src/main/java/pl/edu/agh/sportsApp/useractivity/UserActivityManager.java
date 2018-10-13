package pl.edu.agh.sportsApp.useractivity;

import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
public class UserActivityManager {

    private final Map<String, UserActivityData> activeUsers = new HashMap<>();

    public void addActiveUser(String userName, User user, String token) {
        activeUsers.put(userName, new UserActivityData(token, user));
    }

    public Optional<User> removeActiveUser(User user) {
        for (String username : activeUsers.keySet())
            if (activeUsers.get(username).getUser().equals(user)) {
                return Optional.of(activeUsers.remove(username).getUser());
            }

        return Optional.empty();
    }

    public Optional<UserActivityData> findByUsername(String userName) {
        return ofNullable(activeUsers.get(userName));
    }

}
