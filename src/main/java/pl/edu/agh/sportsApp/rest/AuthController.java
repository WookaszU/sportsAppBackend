package pl.edu.agh.sportsApp.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

@Api(description = "Public controller for login, registration, confirming/resending registration emails.")
@RestController
@RequestMapping("/public/users")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
final class AuthController {

    @NonNull
    AuthenticationService authenticationService;

    @ApiOperation(value = "Register new account.", notes = "After registration confirm email.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Registered, waiting for email confirm."),
            @ApiResponse(code = 400, message = "ResponseCodes = {METHOD_ARGS_NOT_VALID}"),
            @ApiResponse(code = 409, message = "ResponseCodes = {ALREADY_REGISTERED}"),
            @ApiResponse(code = 500, message = "ResponseCodes = {EMAIL_ERROR}")
    })
    @PostMapping("/register")
    public void register(@Valid @RequestBody final RegisterRequestDTO registerRequestDTO) {
        authenticationService.register(registerRequestDTO);
    }

    @ApiOperation(value = "Enable account.", notes = "Triggered by email confirmation link click.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account enabled."),
            @ApiResponse(code = 404, message = "Not found.")
    })
    @GetMapping("/confirm/{token}")
    public String registrationConfirm(@PathVariable("token") final String registerToken) {
        authenticationService.registrationConfirm(registerToken);
        return "Success - need to make here a page with good looking confirm success info.";
    }

    @ApiOperation(value = "Resend a confirm email.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Confirm token resent."),
            @ApiResponse(code = 400, message = "ResponseCodes = {TOKEN_EXPIRED, NOT_REGISTERED, TOKEN_NOT_FOUND}"),
            @ApiResponse(code = 500, message = "ResponseCodes = {EMAIL_ERROR}")
    })
    @PostMapping("/confirm/resend")
    public void resendRegistrationEmail(@Valid @RequestBody final ResendEmailRequestDTO request) {
        authenticationService.resendRegistrationEmail(request);
    }

    @ApiOperation(value = "Login to app.", notes = "It returns token which is required to gain access to api.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful login."),
            @ApiResponse(code = 401, message = "ResponseCodes = {WRONG_LOGIN_OR_PASSWORD, CONFIRM_YOUR_ACCOUNT}")
    })
    @PostMapping("/login")
    public UserTokenState login(@Valid @RequestBody final LoginRequestDTO authenticationRequest) {
        return authenticationService.login(authenticationRequest);
    }

}
