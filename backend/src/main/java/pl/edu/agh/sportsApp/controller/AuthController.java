package pl.edu.agh.sportsApp.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.sportsApp.auth.AuthenticationService;
import pl.edu.agh.sportsApp.controller.Messages.RegisterResponse;
import pl.edu.agh.sportsApp.controller.Messages.ResendEmailRequest;
import pl.edu.agh.sportsApp.controller.Messages.ResponseCode;
import pl.edu.agh.sportsApp.model.Account;
import pl.edu.agh.sportsApp.model.token.Token;
import pl.edu.agh.sportsApp.service.AccountService;
import pl.edu.agh.sportsApp.service.RegisterService;
import pl.edu.agh.sportsApp.service.TokenStorage;

import javax.mail.MessagingException;
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
    @NonNull
    TokenStorage tokenStorage;
    @NonNull
    RegisterService registerService;
    @NonNull
    BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    RegisterResponse register(@RequestBody final Account account) {

        Optional<Account> accountExist = accountService.getAccountByEmail(account.getEmail());

        if(accountExist.isPresent())
            return RegisterResponse.builder()
                    .code(ResponseCode.ALREADY_REGISTERED)
                    .build();

        try {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            registerService.register(account);
            return RegisterResponse.builder()
                    .code(ResponseCode.SUCCESS)
                    .build();
        } catch (MessagingException e) {
            return RegisterResponse.builder()
                    .code(ResponseCode.EMAIL_ERROR)
                    .build();
        }

    }

    @GetMapping("/confirm/{token}")
    RegisterResponse registrationConfirm(@PathVariable("token") final String registerToken){

        Optional<Token> tokenOpt = tokenStorage.getTokenByValue(registerToken);
        if(!tokenOpt.isPresent())
            return RegisterResponse.builder()
                    .code(ResponseCode.TOKEN_NOT_FOUND)
                    .build();

        if(!registerService.confirm(tokenOpt.get()))
            return RegisterResponse.builder()
                    .code(ResponseCode.TOKEN_EXPIRED)
                    .build();

        return RegisterResponse.builder()
                .code(ResponseCode.SUCCESS)
                .build();
    }

    @PostMapping("/confirm/resend")
    RegisterResponse resendRegistrationEmail(@RequestBody final ResendEmailRequest request){

        Optional<Token> previousTokenOpt = tokenStorage.findByRelatedAccountEmail(request.getEmail());

        if(!previousTokenOpt.isPresent())
            return RegisterResponse.builder()
                    .code(ResponseCode.TOKEN_NOT_FOUND)
                    .build();

        try {
            registerService.resendEmail(previousTokenOpt.get());
            return RegisterResponse.builder()
                    .code(ResponseCode.SUCCESS)
                    .build();
        } catch (MessagingException e) {
            return RegisterResponse.builder()
                    .code(ResponseCode.EMAIL_ERROR)
                    .build();
        }

    }

    @PostMapping("/login")
    ResponseEntity login(@RequestBody final Account account) {
        Optional<String> tokenOpt = authentication.login(account.getEmail(), account.getPassword());
        if(tokenOpt.isPresent())
            return new ResponseEntity<>(tokenOpt.get(), HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}
