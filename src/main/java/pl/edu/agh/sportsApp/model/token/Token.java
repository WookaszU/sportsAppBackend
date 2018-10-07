package pl.edu.agh.sportsApp.model.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.agh.sportsApp.model.Account;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity(name = "token")
@NoArgsConstructor
@Data
public class Token {

    @Column(name = "token_id")
    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;

    @Column(name = "type", nullable = false)
    private TokenType type;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "date", nullable= false)
    private ZonedDateTime dateTime;

    @ManyToOne
    @JoinTable(name = "token_account", joinColumns = {@JoinColumn(name = "account_id")})
    private Account relatedAccount;

    public Token(TokenType type, String value, Account relatedAccount, ZonedDateTime dateTime) {
        this.type = type;
        this.value = value;
        this.relatedAccount = relatedAccount;
        this.dateTime = dateTime;
    }
}
