package pl.edu.agh.sportsApp.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.edu.agh.sportsApp.dto.UserDTO;
import pl.edu.agh.sportsApp.model.notification.Notification;
import pl.edu.agh.sportsApp.model.photo.Photo;
import pl.edu.agh.sportsApp.model.photo.ProfilePhoto;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "Users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = {"id", "userPhoto", "tokenIds", "tokens", "eventIds", "events", "userRatings", "rating"})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    private boolean enabled;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private Double rating;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "ratedUser", fetch = FetchType.EAGER)
    private Set<UserRating> userRatings;

    @Setter(AccessLevel.PRIVATE)
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "user", cascade = javax.persistence.CascadeType.ALL,
            orphanRemoval = true)
    private ProfilePhoto userPhoto;

    @Transient
    private Set<Long> tokenIds = new HashSet<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    @Cascade(CascadeType.SAVE_UPDATE)
    @Builder.Default
    private Set<Token> tokens = new HashSet<>();

    @Transient
    private Set<Long> eventIds = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @Where(clause = "START_DATE > NOW()")
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinTable(
            name = "UserEvents",
            joinColumns = {@JoinColumn(name = "users_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    @Builder.Default
    private Set<Event> events = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "relatedUsers")
    private Set<Notification> notifications = new HashSet<>();

    public void setPhoto(ProfilePhoto photo) {
        photo.setUser(this);
        this.setUserPhoto(photo);
    }

    public void deletePhoto() {
        this.setUserPhoto(null);
    }

    public void addUserRating(UserRating userRating) {
        userRatings.add(userRating);
    }

    public void removeUserRating(UserRating userRating) {
        userRatings.remove(userRating);
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public void deleteNotification(Notification notification) {
        notifications.remove(notification);
    }

    public UserDTO mapToDTO() {
        UserDTO userDTO = UserDTO.builder()
                .id(this.getId())
                .email(this.getEmail())
                .firstName(this.getFirstName())
                .lastName(this.getLastName())
                .rating(this.getRating())
                .currentEventParticipantIds(this.getEvents().stream()
                        .map(Event::getId)
                        .collect(Collectors.toList()))
                .build();
        Photo photo = this.getUserPhoto();
        if (photo != null) {
            userDTO.setPhotoId(photo.getPhotoId());
        }
        return userDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
