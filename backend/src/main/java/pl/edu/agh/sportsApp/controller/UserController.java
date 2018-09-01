package pl.edu.agh.sportsApp.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.sportsApp.auth.AuthenticationService;
import pl.edu.agh.sportsApp.model.Account;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
final class UserController {

    @NonNull
    AuthenticationService authentication;

    @GetMapping("/current")
    Account getCurrent(@AuthenticationPrincipal final Account account) {
        return account;
    }

    @GetMapping("/logout")
    boolean logout(@AuthenticationPrincipal final Account account) {
        authentication.logout(account);
        return true;
    }

}
