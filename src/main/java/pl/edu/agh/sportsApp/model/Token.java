package pl.edu.agh.sportsApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity(name = "token")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @NotNull
    private TokenType type;

    @NotNull
    private String value;

    @NotNull
    private ZonedDateTime dateTime;

    @Column(name = "owner_id")
    private Long ownerId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private User owner;

    public enum TokenType {
        REGISTER_CONFIRM
    }
}
