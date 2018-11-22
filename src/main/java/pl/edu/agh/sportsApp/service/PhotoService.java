package pl.edu.agh.sportsApp.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.sportsApp.dto.EventPhotosIdsRequestDTO;
import pl.edu.agh.sportsApp.dto.ResponseCode;
import pl.edu.agh.sportsApp.exceptionHandler.exceptions.PhotoProcessingException;
import pl.edu.agh.sportsApp.filemanager.PhotoManager;
import pl.edu.agh.sportsApp.model.Event;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.model.photo.EventPhoto;
import pl.edu.agh.sportsApp.model.photo.Photo;
import pl.edu.agh.sportsApp.model.photo.ProfilePhoto;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PhotoService {

    PhotoManager photoManager;
    UserService userService;
    EventService eventService;
    Integer maxProfilePhotoSize;
    Integer maxEventPhotoSize;

    @Autowired
    public PhotoService(PhotoManager photoManager, UserService userService, EventService eventService,
                        @Value("${app.files.photo.event.max-size}") Integer maxEventPhotoSize,
                        @Value("${app.files.photo.profile.max-size}") Integer maxProfilePhotoSize) {
        this.photoManager = photoManager;
        this.userService = userService;
        this.eventService = eventService;
        this.maxProfilePhotoSize = maxProfilePhotoSize;
        this.maxEventPhotoSize = maxEventPhotoSize;
    }

    private void setProfilePhoto(User user, MultipartFile file) {
        Photo currentPhoto = user.getUserPhoto();

        ProfilePhoto photo;
        try {
            photo = photoManager.saveProfilePhoto(file);
        } catch (IOException e) {
            throw new PhotoProcessingException(ResponseCode.MEDIA_SERVICE_NOT_AVAILABLE.name());
        }
        if(currentPhoto != null)
            photo.setId(currentPhoto.getId());
        userService.setUserAvatar(user, photo);
    }

    public void removeProfilePhoto(User user) {
        Photo photo = user.getUserPhoto();
        if (photo == null)
            throw new EntityNotFoundException(ResponseCode.RESOURCE_NOT_FOUND.name());
        userService.removeUserAvatar(user);
        photoManager.removePhotoFromServerStorage(photo);
    }

    private void addEventPhoto(Event event, MultipartFile file) {
        EventPhoto photo;
        try {
            photo = photoManager.saveEventPhoto(file);
        } catch (IOException e) {
            throw new PhotoProcessingException(ResponseCode.MEDIA_SERVICE_NOT_AVAILABLE.name());
        }

        eventService.addEventPhoto(event, photo);

    }

    public void removeEventPhoto(Long eventId, String photoId) {
        Optional<Event> eventOpt = eventService.findEventById(eventId);
        if(!eventOpt.isPresent())
            throw new EntityNotFoundException(ResponseCode.RESOURCE_NOT_FOUND.name());

        Event relatedEvent = eventOpt.get();
        EventPhoto photoToDelete = null;
        for(EventPhoto photo : relatedEvent.getEventPhotos()){
            if(photo.getPhotoId().equals(photoId)) {
                photoToDelete = photo;
                break;
            }
        }

        if(photoToDelete == null)
            throw new EntityNotFoundException(ResponseCode.RESOURCE_NOT_FOUND.name());

        eventService.removeEventPhoto(relatedEvent, photoToDelete);
        photoManager.removePhotoFromServerStorage(photoToDelete);
    }

    public void handleUserAvatarUpload(final MultipartFile file, final User user){
        if (file == null || file.isEmpty())
            throw new PhotoProcessingException(ResponseCode.EMPTY_FILE.name());

        if (file.getSize() > maxProfilePhotoSize)
            throw new PhotoProcessingException(ResponseCode.FILE_TOO_BIG.name());

        if(file.getContentType() == null || !file.getContentType().matches("image/(.*)"))
            throw new PhotoProcessingException(ResponseCode.WRONG_FORMAT.name());

        setProfilePhoto(user, file);
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

    public Resource serveUserPhoto(User user){
        if(user.getUserPhoto() == null)
            throw new EntityNotFoundException(ResponseCode.RESOURCE_NOT_FOUND.name());
        return serveLowQualityPhoto(user.getUserPhoto().getPhotoId());
    }

    public void handleEventPhotoUpload(MultipartFile file, String eventId) {
        if (file == null || file.isEmpty())
            throw new PhotoProcessingException(ResponseCode.EMPTY_FILE.name());

        if(file.getContentType() == null || !file.getContentType().matches("image/(.*)"))
            throw new PhotoProcessingException(ResponseCode.WRONG_FORMAT.name());

        Optional<Event> eventOpt = eventService.findEventById(Long.parseLong(eventId));

        if(!eventOpt.isPresent())
            throw new EntityNotFoundException(ResponseCode.RESOURCE_NOT_FOUND.name());

        if (file.getSize() > maxEventPhotoSize)
            throw new PhotoProcessingException(ResponseCode.FILE_TOO_BIG.name());

        addEventPhoto(eventOpt.get(), file);
    }

    public EventPhotosIdsRequestDTO handleEventPhotosIdsRequest(Long eventId){
        Optional<Event> eventOpt = eventService.findEventById(eventId);

        if(!eventOpt.isPresent())
            throw new EntityNotFoundException(ResponseCode.RESOURCE_NOT_FOUND.name());

        Set<EventPhoto> eventPhotos = eventOpt.get().getEventPhotos();
        EventPhotosIdsRequestDTO eventPhotosIdsRequestDTO = new EventPhotosIdsRequestDTO();

        for(EventPhoto photo : eventPhotos)
            eventPhotosIdsRequestDTO.getPhotoIds().add(photo.getPhotoId());

        return eventPhotosIdsRequestDTO;
    }

}