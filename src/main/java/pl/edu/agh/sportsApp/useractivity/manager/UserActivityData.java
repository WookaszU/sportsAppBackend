package pl.edu.agh.sportsApp.useractivity.manager;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pl.edu.agh.sportsApp.model.User;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserActivityData {

    String token;
    User user;

}
