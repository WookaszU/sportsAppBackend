package pl.edu.agh.sportsApp.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pl.edu.agh.sportsApp.model.chat.Chat;

@NoRepositoryBean
public interface ChatRepository<T extends Chat> extends JpaRepository<T, Long> {

}
