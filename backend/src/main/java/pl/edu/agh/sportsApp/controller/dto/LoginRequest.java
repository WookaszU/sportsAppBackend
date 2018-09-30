package pl.edu.agh.sportsApp.controller.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class LoginRequest {

    String email;
    String password;

}
