package pl.edu.agh.sportsApp.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.dateservice.DateService;
import pl.edu.agh.sportsApp.emailsender.EmailSender;
import pl.edu.agh.sportsApp.model.Account;
import pl.edu.agh.sportsApp.model.token.Token;
import pl.edu.agh.sportsApp.model.token.TokenType;

import javax.mail.MessagingException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegisterService {

    @NonNull
    AccountService accountService;
    @NonNull
    TokenStorage tokenStorage;
    @NonNull
    EmailSender EmailSender;
    @NonNull
    DateService dateService;


    public void register(Account account) throws MessagingException, MailAuthenticationException {
        account.setEnabled(false);
        Account newAccount = accountService.saveAccount(account);

        String registerToken = EmailSender.sendRegisterEmail(newAccount);
        tokenStorage.saveToken(new Token(TokenType.REGISTER_CONFIRM, registerToken, newAccount, dateService.now()));
    }

    public boolean confirm(Token confirmToken){
        if(confirmToken.getDateTime().isBefore(dateService.now().minusDays(1)))
            return false;
        Account confirmedAcc = confirmToken.getRelatedAccount();
        confirmedAcc.setEnabled(true);
        accountService.saveAccount(confirmedAcc);
        tokenStorage.removeToken(confirmToken.getId());
        return true;
    }

    public void resendEmail(Token prevToken) throws MessagingException {
        tokenStorage.removeToken(prevToken.getId());
        String registerToken = EmailSender.sendRegisterEmail(prevToken.getRelatedAccount());
        tokenStorage.saveToken(new Token(TokenType.REGISTER_CONFIRM,
                                         registerToken,
                                         prevToken.getRelatedAccount(),
                                         dateService.now()));
    }


}
