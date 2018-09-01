package pl.edu.agh.sportsApp.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.sportsApp.auth.AuthenticationService;
import pl.edu.agh.sportsApp.auth.exception.LoginFailedException;
import pl.edu.agh.sportsApp.model.Account;
import pl.edu.agh.sportsApp.service.AccountService;
import pl.edu.agh.sportsApp.service.UserActivityService;

import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/public/users")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
final class AuthController {

    @NonNull
    AuthenticationService authentication;
    @NonNull
    AccountService accountService;

    @PostMapping("/register")
    String register(@RequestBody final Account account) {

        accountService.saveAccount(account);

        return login(account);
    }

    @PostMapping("/login")
    String login(@RequestBody final Account account) {

        return authentication
                .login(account.getEmail(), account.getPassword())
                .orElseThrow(() -> new LoginFailedException());
    }

}
