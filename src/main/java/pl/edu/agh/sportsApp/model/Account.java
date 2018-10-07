package pl.edu.agh.sportsApp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
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
@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class Account implements UserDetails {

    @Column(name = "account_id")
    @Id
    @GeneratedValue
    @JsonIgnore
    int id;

    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "enabled", nullable = false)
    boolean enabled;

    @Column(name = "firstName", nullable = false)
    String firstName;

    @Column(name = "lastName", nullable = false)
    String lastName;

    @OneToOne(fetch=FetchType.LAZY)
    Photo userPhoto;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "account_event", joinColumns = {@JoinColumn(name = "account_id")})
    List<Event> events;

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
        return enabled;
    }

}
