package pl.edu.agh.sportsApp.model.chat;

import lombok.*;
import org.hibernate.annotations.Cascade;
import pl.edu.agh.sportsApp.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter
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
