package pl.edu.agh.sportsApp.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.dateservice.DateService;
import pl.edu.agh.sportsApp.dto.ResponseCode;
import pl.edu.agh.sportsApp.emailsender.EmailSender;
import pl.edu.agh.sportsApp.emailsender.tokengenerator.TokenGenerator;
import pl.edu.agh.sportsApp.exceptionHandler.exceptions.RegisterException;
import pl.edu.agh.sportsApp.model.Token;
import pl.edu.agh.sportsApp.model.User;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegisterService {

    @NonNull
    UserService userService;
    @NonNull
    TokenStorage tokenStorage;
    @NonNull
    EmailSender EmailSender;
    @NonNull
    DateService dateService;
    @NonNull
    TokenGenerator tokenGenerator;


    public void register(User user) {
        if(userService.getUserByEmail(user.getEmail()).isPresent())
            throw new RegisterException(ResponseCode.ALREADY_REGISTERED.name());
        String registerToken = tokenGenerator.generate(user);
        user.setEnabled(false);
        User newUser = userService.saveUser(user);
        Token token = Token.builder()
                .type(Token.TokenType.REGISTER_CONFIRM)
                .value(registerToken)
                .dateTime(dateService.now())
                .owner(newUser)
                .ownerId(newUser.getId())
                .build();
        tokenStorage.saveToken(token);
        EmailSender.sendRegisterEmail(user.getEmail(), registerToken);
    }

    public boolean confirm(Token confirmToken) {
        if (confirmToken.getDateTime().isBefore(dateService.now().minusDays(1)))
            return false;
        User confirmedAcc = confirmToken.getOwner();
        confirmedAcc.setEnabled(true);
        userService.saveUser(confirmedAcc);
        tokenStorage.removeToken(confirmToken.getId());
        return true;
    }

    public void resendEmail(Token prevToken) {
        String registerToken = tokenGenerator.generate(prevToken.getOwner());
        tokenStorage.removeToken(prevToken.getId());
        Token newToken = Token.builder()
                .type(Token.TokenType.REGISTER_CONFIRM)
                .value(registerToken)
                .dateTime(dateService.now())
                .owner(prevToken.getOwner())
                .ownerId(prevToken.getOwnerId())
                .build();
        tokenStorage.saveToken(newToken);
        EmailSender.sendRegisterEmail(prevToken.getOwner().getEmail(), registerToken);
    }


}
