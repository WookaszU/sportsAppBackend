package pl.edu.agh.sportsApp.auth.exception;


import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason = "Wrong username or password.")
public class LoginFailedException extends RuntimeException {

}
