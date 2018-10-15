package pl.edu.agh.sportsApp.rest;

import io.swagger.annotations.*;
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

@Api(description = "Controller with basic functions for logged users.")
@RestController
@RequestMapping("/users")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
final class UserController {

    @NonNull
    private final AuthenticationService authenticationService;
    @NonNull
    private final UserService userService;

    @ApiOperation(value = "Get current user info.", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User data returned."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
    })
    @GetMapping("/current")
    public UserDTO getCurrent(@ApiIgnore @AuthenticationPrincipal final User user) {
        return user.mapToDTO();
    }

    @ApiOperation(value = "Logout from application.", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Current user logged out."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
    })
    @GetMapping("/logout")
    public boolean logout(@ApiIgnore @AuthenticationPrincipal final User user) {
        return authenticationService.logout(user).isPresent();
    }

    @ApiOperation(value = "Unregister from application.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Current user unregistered."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
    })
    @DeleteMapping("/unregister")
    public void unregister(@ApiIgnore @AuthenticationPrincipal final User user) {
        userService.removeUser(user.getId());
    }

    @ApiOperation(value = "Update current user data.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Current user data updated."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
    })
    @PutMapping
    public void updateUser(@Valid @RequestBody UserModifyDTO userDTO,
                           @ApiIgnore @AuthenticationPrincipal final User user) {
        userService.updateUser(userDTO, user);
    }

    @ApiOperation(value = "Get user profile by userId.", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User returned."),
            @ApiResponse(code = 400, message = "Wrong user id."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
            @ApiResponse(code = 404, message = "User not found.")
    })
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable("id") Long userId) {
        return userService.getUserById(userId).mapToDTO();
    }
}
