package pl.edu.agh.sportsApp.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.sportsApp.dto.ResourceType;
import pl.edu.agh.sportsApp.dto.ResponseCode;
import pl.edu.agh.sportsApp.dto.UploadResponseDTO;
import pl.edu.agh.sportsApp.exceptionHandler.exceptions.PhotoProcessingException;
import pl.edu.agh.sportsApp.filemanager.PhotoManager;
import pl.edu.agh.sportsApp.model.Photo;
import pl.edu.agh.sportsApp.model.User;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PhotoService {

    PhotoManager photoManager;
    UserService userService;

    private Photo setProfilePhoto(User user, MultipartFile file) {
        if(user.getUserPhoto() != null)
            removeProfilePhoto(user);

        Photo photo;
        try {
            photo = photoManager.savePhoto(file);
        } catch (IOException e) {
            throw new PhotoProcessingException(ResponseCode.MEDIA_SERVICE_INACCESIBLE.name());
        }

        user.setUserPhoto(photo);
        userService.saveUser(user);
        return photo;
    }

    public void removeProfilePhoto(User user) {
        Photo photo = user.getUserPhoto();
        if (photo == null)
            throw new EntityNotFoundException(ResponseCode.RESOURCE_NOT_FOUND.name());
        user.setUserPhoto(null);
        userService.saveUser(user);
        photoManager.removePhoto(photo);
    }

    public UploadResponseDTO handleUserAvatarUpload(final MultipartFile file, final User user){
        if (file == null || file.isEmpty())
            throw new PhotoProcessingException(ResponseCode.EMPTY_FILE.name());

        if (file.getSize() > 1048576)
            throw new PhotoProcessingException(ResponseCode.FILE_TOO_BIG.name());

        return new UploadResponseDTO(ResourceType.PHOTO, setProfilePhoto(user, file).getPhotoId());
    }

    public Resource serveHighQualityPhoto(String resourceId){
        Optional<Resource> resourceOpt = photoManager.loadHighResolutionPhoto(resourceId);
        return resourceOpt.orElseThrow(
                () -> new EntityNotFoundException(ResponseCode.RESOURCE_NOT_FOUND.name()));
    }

    public Resource serveLowQualityPhoto(String resourceId){
        Optional<Resource> resourceOpt = photoManager.loadLowResolutionPhoto(resourceId);
        return resourceOpt.orElseThrow(
                () -> new EntityNotFoundException(ResponseCode.RESOURCE_NOT_FOUND.name()));
    }

}
