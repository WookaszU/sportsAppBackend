package pl.edu.agh.sportsApp.rest;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.sportsApp.auth.AuthenticationService;
import pl.edu.agh.sportsApp.dto.UserDTO;
import pl.edu.agh.sportsApp.dto.UserModifyDTO;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.service.UserService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
final class UserController {

    @NonNull
    private final AuthenticationService authenticationService;
    @NonNull
    private final UserService userService;

    @ApiOperation(value = "Get current user info.")
    @GetMapping("/current")
    public UserDTO getCurrent(@ApiIgnore @AuthenticationPrincipal final User user) {
        return user.mapToDTO();
    }

    @ApiOperation(value = "Logout from application.")
    @GetMapping("/logout")
    public boolean logout(@ApiIgnore @AuthenticationPrincipal final User user) {
        return authenticationService.logout(user).isPresent();
    }

    @ApiOperation(value = "Unregister from application.")
    @DeleteMapping("/unregister")
    public void unregister(@ApiIgnore @AuthenticationPrincipal final User user) {
        userService.removeUser(user.getId());
    }

    @PutMapping
    public void updateUser(@Valid @RequestBody UserModifyDTO userDTO,
                           @AuthenticationPrincipal final User user) {
        userService.updateUser(userDTO, user);
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable("id") Long userId) {
        return userService.getUserById(userId).mapToDTO();
    }
}
