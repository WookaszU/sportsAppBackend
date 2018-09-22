package pl.edu.agh.sportsApp.controller;

import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value="Get current user info.")
    @GetMapping("/current")
    Account getCurrent(@AuthenticationPrincipal final Account account) {
        return account;
    }

    @ApiOperation(value="Logout from application.")
    @GetMapping("/logout")
    boolean logout(@AuthenticationPrincipal final Account account) {
        return authentication.logout(account).isPresent();
    }

}
