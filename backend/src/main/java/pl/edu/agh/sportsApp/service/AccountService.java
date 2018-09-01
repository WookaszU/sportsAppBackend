package pl.edu.agh.sportsApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.model.Account;
import pl.edu.agh.sportsApp.repository.AccountRepository;

import java.util.Optional;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> getAccountById(Integer id){
        return Optional.ofNullable(accountRepository.findById(id));
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public void removeAccount(int id) {
        accountRepository.removeAccountById(id);
    }

    public Optional<Account> getAccountByEmail(String email){return Optional.ofNullable(accountRepository.findByEmail(email));}
}
