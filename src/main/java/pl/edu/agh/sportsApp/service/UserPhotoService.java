package pl.edu.agh.sportsApp.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.sportsApp.exceptionHandler.exceptions.InvalidPhotoProportionsException;
import pl.edu.agh.sportsApp.filemanager.PhotoManager;
import pl.edu.agh.sportsApp.model.Photo;
import pl.edu.agh.sportsApp.model.User;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserPhotoService {

    PhotoManager photoManager;
    UserService userService;

    public Photo setProfilePhoto(User user, MultipartFile file) throws InvalidPhotoProportionsException, IOException {
        removeProfilePhoto(user);
        Photo photo = photoManager.savePhoto(file);
        user.setUserPhoto(photo);
        userService.saveUser(user);
        return photo;
    }

    public Optional<Photo> removeProfilePhoto(User user) {
        Photo photo = user.getUserPhoto();
        if (photo == null)
            return Optional.empty();
        user.setUserPhoto(null);
        userService.saveUser(user);
        return photoManager.removePhoto(photo);
    }

}
