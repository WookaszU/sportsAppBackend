package pl.edu.agh.sportsApp.useractivity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.edu.agh.sportsApp.model.Account;

@Getter @Setter
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class UserActivityData {

    String token;
    Account account;

}
