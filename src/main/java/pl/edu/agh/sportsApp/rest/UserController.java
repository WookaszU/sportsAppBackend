package pl.edu.agh.sportsApp.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.sportsApp.auth.AuthenticationService;
import pl.edu.agh.sportsApp.dto.UserDTO;
import pl.edu.agh.sportsApp.dto.UserModifyDTO;
import pl.edu.agh.sportsApp.dto.UserProfileViewDTO;
import pl.edu.agh.sportsApp.dto.UserRatingListDTO;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.repository.event.projection.EventData;
import pl.edu.agh.sportsApp.repository.user.projection.UserData;
import pl.edu.agh.sportsApp.service.UserService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Api(description = "Controller with basic functions for logged users.")
@RestController
@RequestMapping("/users")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
final class UserController {

    @NonNull
    AuthenticationService authenticationService;
    @NonNull
    UserService userService;

    @ApiOperation(value = "Get current user info.",
            notes = "Returns information about current user e.g. user photoId. ", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User data returned."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
    })
    @GetMapping("/current")
    public UserDTO getCurrent(@ApiIgnore @AuthenticationPrincipal final User user) {
        return userService.getUserById(user.getId()).mapToDTO();
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

    @ApiOperation(value = "Get current user active events.",
            notes = "If not existing userId was given, the result will be an empty list.",
            response = EventData.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Events list returned."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
    })
    @GetMapping("/myEventsList")
    public List<EventData> getUserActiveEvents(@ApiIgnore @AuthenticationPrincipal final User user) {
        return userService.getUserActiveEvents(user);
    }

    @ApiOperation(value = "Get user historic events.",
            notes = "Endpoint for current user.",
            response = EventData.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Events list returned."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
    })
    @GetMapping("/myHistoricEventsList")
    public List<EventData> getMyHistoricEvents(@ApiIgnore @AuthenticationPrincipal final User user) {
        return userService.getUserHistoricEvents(user.getId());
    }

    @ApiOperation(value = "Get given user historic events.",
            notes = "If not existing userId was given, the result will be an empty list.",
            response = EventData.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Events list returned."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
    })
    @GetMapping("/userHistory/{userId}")
    public List<EventData> getUserHistoricEvents(@PathVariable Long userId) {
        return userService.getUserHistoricEvents(userId);
    }

    @ApiOperation(value = "Get user ratings information.",
            notes = "Returns user ratings. If not existing userId was given returns empty list.",
            response = UserRatingListDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User ratings successfully returned."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
    })
    @GetMapping("/ratings/{userId}")
    public UserRatingListDTO getUserRatings(@PathVariable Long userId) {
        return userService.getUserRatings(userId);
    }

    @ApiOperation(value = "Returns data need to construct profile view for user with given id.",
            response = UserProfileViewDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User ratings successfully returned."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
    })
    @GetMapping("/profile/{userId}")
    public UserProfileViewDTO getUserProfile(@PathVariable Long userId) {
        return userService.getUserProfile(userId);
    }

}
