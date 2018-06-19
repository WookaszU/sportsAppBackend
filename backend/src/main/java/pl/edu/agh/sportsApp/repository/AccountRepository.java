package pl.edu.agh.sportsApp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.sportsApp.model.Account;

@Transactional
public interface AccountRepository extends CrudRepository<Account, Integer> {
    Account findById(Integer accountId);

    void removeAccountById(Integer accountID);

    Account findByEmail(String email);
}
