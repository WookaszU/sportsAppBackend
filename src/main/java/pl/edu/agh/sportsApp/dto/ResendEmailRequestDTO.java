package pl.edu.agh.sportsApp.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResendEmailRequestDTO {

    @Email
    @NotNull
    String email;

}
