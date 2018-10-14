package pl.edu.agh.sportsApp.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginRequestDTO {

    @NotNull
    private String email;

    @NotNull
    private String password;

}
