package pl.edu.agh.sportsApp.model.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import pl.edu.agh.sportsApp.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "privateChat")
public class PrivateChat extends Chat {

    private LocalDateTime creationTime;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinTable(
            name = "UserPrivateChat",
            joinColumns = {@JoinColumn(name = "private_chat_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    @Builder.Default
    @MapKeyColumn(name = "id")
    private Map<Long, User> participants = new HashMap<>();

}
