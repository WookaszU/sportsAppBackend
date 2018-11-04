package pl.edu.agh.sportsApp.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.dto.UserModifyDTO;
import pl.edu.agh.sportsApp.dto.UserRatingListDTO;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.model.photo.ProfilePhoto;
import pl.edu.agh.sportsApp.repository.event.EventRepository;
import pl.edu.agh.sportsApp.repository.event.projection.EventData;
import pl.edu.agh.sportsApp.repository.user.UserRepository;
import pl.edu.agh.sportsApp.repository.user.projection.UserRatingData;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    @NonNull
    UserRepository userRepository;
    @NonNull
    EventRepository eventRepository;
    @NonNull
    BCryptPasswordEncoder passwordEncoder;

    public User getUserById(Long id) {
        return userRepository.getOne(id);
    }

    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public void setUserAvatar(User user, ProfilePhoto profilePhoto){
        user.setPhoto(profilePhoto);
        userRepository.save(user);
    }

    public void removeUserAvatar(User user) {
        user.deletePhoto();
        userRepository.save(user);
    }

    public void updateUser(UserModifyDTO userDTO, User user) {
        if (userDTO.getFirstName() != null) user.setFirstName(userDTO.getFirstName());
        if (userDTO.getLastName() != null) user.setLastName(userDTO.getLastName());
        if (userDTO.getPassword() != null) user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);
    }

    public List<EventData> getUserActiveEvents(User user) {
        return eventRepository.getActualUserEvents(user.getId());
    }

    public List<EventData> getUserHistoricEvents(Long userId) {
        return eventRepository.getArchivedUserEvents(userId);
    }

    public UserRatingListDTO getUserProfile(Long userId) {
        List<UserRatingData> userRatings = userRepository.getUserRatingsWithEvaluativeNames(userId);
        return new UserRatingListDTO(userRatings);
    }

}
