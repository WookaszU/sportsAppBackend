package pl.edu.agh.sportsApp.controller.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class RegisterRequest {

    String email;
    String password;
    String firstName;
    String lastName;

}
