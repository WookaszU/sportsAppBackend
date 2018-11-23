package pl.edu.agh.sportsApp.controller;

import io.swagger.annotations.Api;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.edu.agh.sportsApp.auth.AuthenticationService;
import springfox.documentation.annotations.ApiIgnore;

import static lombok.AccessLevel.PRIVATE;

@ApiIgnore
@Api(description = "Returns html connected to registration process.")
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RegisterConfirmController {

    @NonNull
    AuthenticationService authenticationService;

    @RequestMapping("/public/users/confirm/{token}")
    public String registrationConfirm(@PathVariable("token") final String registerToken) {
        authenticationService.registrationConfirm(registerToken);
        return "registrationConfirm.html";
    }

}