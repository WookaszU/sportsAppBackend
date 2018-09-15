package pl.edu.agh.sportsApp.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.model.Account;
import pl.edu.agh.sportsApp.repository.AccountRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {

    @NonNull
    AccountRepository accountRepository;

    public Optional<Account> getAccountById(Integer id){
        return Optional.ofNullable(accountRepository.findById(id));
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public void removeAccount(int id) {
        accountRepository.removeAccountById(id);
    }

    public Optional<Account> getAccountByEmail(String email){
        return Optional.ofNullable(accountRepository.findByEmail(email));
    }
}
