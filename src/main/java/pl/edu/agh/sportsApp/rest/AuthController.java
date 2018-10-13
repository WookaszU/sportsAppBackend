package pl.edu.agh.sportsApp.rest;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.sportsApp.auth.AuthenticationService;
import pl.edu.agh.sportsApp.dto.LoginRequestDTO;
import pl.edu.agh.sportsApp.dto.RegisterRequestDTO;
import pl.edu.agh.sportsApp.dto.ResendEmailRequestDTO;
import pl.edu.agh.sportsApp.dto.UserTokenState;

import javax.validation.Valid;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/public/users")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
final class AuthController {

    @NonNull
    AuthenticationService authenticationService;

    @ApiOperation(value = "Register new account. After registration confirm email...")
    @PostMapping("/register")
    public void register(@Valid @RequestBody final RegisterRequestDTO registerRequestDTO) {
        authenticationService.register(registerRequestDTO);
    }

    @ApiOperation(value = "Email confirmation link clicked. Enable account.")
    @GetMapping("/confirm/{token}")
    public void registrationConfirm(@PathVariable("token") final String registerToken) {
        authenticationService.registrationConfirm(registerToken);
    }

    @ApiOperation(value = "Resend a confirm email.")
    @PostMapping("/confirm/resend")
    public void resendRegistrationEmail(@RequestBody final ResendEmailRequestDTO request) {
        authenticationService.resendRegistrationEmail(request);
    }

    @ApiOperation(value = "Login to app. Returns token.")
    @PostMapping("/login")
    public UserTokenState login(@Valid @RequestBody final LoginRequestDTO authenticationRequest) {
        return authenticationService.login(authenticationRequest);

    }

}
