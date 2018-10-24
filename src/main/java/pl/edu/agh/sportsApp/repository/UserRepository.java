package pl.edu.agh.sportsApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.sportsApp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
