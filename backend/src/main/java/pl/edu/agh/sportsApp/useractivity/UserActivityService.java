package pl.edu.agh.sportsApp.useractivity;

import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.model.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
public class UserActivityService {

    private final Map<String, UserActivityData> activeUsers = new HashMap<>();

    public void addActiveUser(String userName, Account account, String token){
        activeUsers.put(userName, new UserActivityData(token, account));
    }

    public Optional<Account> removeActiveUser(Account account){
        for(String username: activeUsers.keySet())
            if(activeUsers.get(username).getAccount().equals(account)){
                return Optional.of(activeUsers.remove(username).getAccount());
            }

        return Optional.empty();
    }

    public Optional<UserActivityData> findByUsername(String userName){
        return ofNullable(activeUsers.get(userName));
    }

}
