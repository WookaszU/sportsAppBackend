package pl.edu.agh.sportsApp.controller;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.sportsApp.auth.AuthenticationService;
import pl.edu.agh.sportsApp.controller.dto.*;
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

    @ApiOperation(value="Register new account. After registration confirm email...")
    @PostMapping("/register")
    ResponseEntity register(@RequestBody final RegisterRequest registerRequest) {

        if(registerRequest.getEmail() == null || registerRequest.getFirstName() == null ||
                registerRequest.getLastName() == null || registerRequest.getPassword() == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        if(!EmailValidator.getInstance().isValid(registerRequest.getEmail()))
            return new ResponseEntity<>(ResponseCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);

        Optional<Account> accountExist = accountService.getAccountByEmail(registerRequest.getEmail());

        if(accountExist.isPresent())
            return new ResponseEntity<>(ResponseCode.ALREADY_REGISTERED, HttpStatus.CONFLICT);

        try {
            Account account = Account.builder()
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .build();
            registerService.register(account);
            return new ResponseEntity(HttpStatus.OK);
        } catch (MessagingException | MailAuthenticationException e) {
            return new ResponseEntity<>(ResponseCode.EMAIL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @ApiOperation(value="Email confirmation link clicked. Enable account.")
    @GetMapping("/confirm/{token}")
    ResponseEntity registrationConfirm(@PathVariable("token") final String registerToken){

        Optional<Token> tokenOpt = tokenStorage.getTokenByValue(registerToken);
        if(!tokenOpt.isPresent())
            return new ResponseEntity<>(ResponseCode.TOKEN_NOT_FOUND, HttpStatus.BAD_REQUEST);

        if(!registerService.confirm(tokenOpt.get()))
            return new ResponseEntity<>(ResponseCode.TOKEN_EXPIRED, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(ResponseCode.SUCCESS, HttpStatus.OK);
    }

    @ApiOperation(value="Resend a confirm email.")
    @PostMapping("/confirm/resend")
    ResponseEntity resendRegistrationEmail(@RequestBody final ResendEmailRequest request){

        if(!EmailValidator.getInstance().isValid(request.getEmail()))
            return new ResponseEntity<>(ResponseCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);

        Optional<Token> previousTokenOpt = tokenStorage.findByRelatedAccountEmail(request.getEmail());

        if(!previousTokenOpt.isPresent())
            return new ResponseEntity<>(ResponseCode.NOT_REGISTERED, HttpStatus.BAD_REQUEST);

        try {
            registerService.resendEmail(previousTokenOpt.get());
            return new ResponseEntity<>(ResponseCode.SUCCESS, HttpStatus.OK);
        } catch (MessagingException e) {
            return new ResponseEntity<>(ResponseCode.EMAIL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @ApiOperation(value="Login to app. Returns token.")
    @PostMapping("/login")
    ResponseEntity login(@RequestBody final LoginRequest request) {

        if(request.getEmail() == null || request.getPassword() == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        Optional<Account> accountOpt = accountService.getAccountByEmail(request.getEmail());
        if(!accountOpt.isPresent())
            return new ResponseEntity<>(ResponseCode.WRONG_LOGIN_OR_PASSWORD, HttpStatus.BAD_REQUEST);

        Account account = accountOpt.get();

        if(!account.isEnabled())
            return new ResponseEntity<>(ResponseCode.CONFIRM_YOUR_ACCOUNT, HttpStatus.BAD_REQUEST);

        Optional<String> tokenOpt = authentication.login(account, request.getEmail(), request.getPassword());

        if(tokenOpt.isPresent())
            return new ResponseEntity<>(tokenOpt.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(ResponseCode.WRONG_LOGIN_OR_PASSWORD ,HttpStatus.BAD_REQUEST);

    }

}
