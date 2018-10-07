package pl.edu.agh.sportsApp.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class RegisterResponse {

    ResponseCode code;

}
