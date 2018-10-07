package pl.edu.agh.sportsApp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.sportsApp.model.Account;

import java.util.Optional;

@Repository
@Transactional
public interface AccountRepository extends CrudRepository<Account, Integer> {

    Account findById(Integer accountId);

    void removeAccountById(Integer accountID);

    Account findByEmail(String email);

}
