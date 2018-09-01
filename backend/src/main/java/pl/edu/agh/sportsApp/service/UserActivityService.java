package pl.edu.agh.sportsApp.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.model.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
public class UserActivityService {

    private final Map<String, Account> activeUsers = new HashMap<>();

    public void addActiveUser(String userName, Account account){
        activeUsers.put(userName, account);
    }

    public Optional<Account> removeActiveUser(Account account){
        for(String token: activeUsers.keySet())
            if(activeUsers.get(token).equals(account)){
                return Optional.of(activeUsers.remove(token));
            }

        return Optional.empty();
    }

    public Optional<Account> findByUsername(String userName){
        return ofNullable(activeUsers.get(userName));
    }

}
