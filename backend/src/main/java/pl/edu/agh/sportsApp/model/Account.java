package pl.edu.agh.sportsApp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.assertj.core.util.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Entity(name = "account")
@NoArgsConstructor
public class Account implements UserDetails {

    @Getter
    @Column(name = "account_id")
    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;

    @Getter @Setter
    @Column(name = "email", nullable = false)
    private String email;

    @Setter
    @Column(name = "password", nullable = false)
    private String password;

    @Getter @Setter
    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Getter @Setter
    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Getter @Setter
    @Lob
    @Column(name = "image", length = Integer.MAX_VALUE)
    private byte[] image;

    @Getter @Setter
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "account_event", joinColumns = {@JoinColumn(name = "account_id")})
    private List<Event> events;

    public Account(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        events = Lists.newArrayList();
    }

    @JsonCreator
    public Account(@JsonProperty("email") String email,
                   @JsonProperty("password") String password,
                   @JsonProperty("firstName") String firstName,
                   @JsonProperty("lastName") String lastName) {
        super();
        this.email = requireNonNull(email);
        this.password = requireNonNull(password);
        this.firstName = requireNonNull(firstName);
        this.lastName = requireNonNull(lastName);
        events = Lists.newArrayList();
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
