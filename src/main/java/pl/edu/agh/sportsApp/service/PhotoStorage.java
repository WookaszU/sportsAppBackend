package pl.edu.agh.sportsApp.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.model.photo.EventPhoto;
import pl.edu.agh.sportsApp.model.photo.Photo;
import pl.edu.agh.sportsApp.model.photo.ProfilePhoto;
import pl.edu.agh.sportsApp.repository.photo.EventPhotoRepository;
import pl.edu.agh.sportsApp.repository.photo.PhotoRepository;
import pl.edu.agh.sportsApp.repository.photo.ProfilePhotoRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PhotoStorage {

    ProfilePhotoRepository profilePhotoRepository;
    EventPhotoRepository eventPhotoRepository;
    PhotoRepository photoRepository;

    public ProfilePhoto save(ProfilePhoto photo) {
        return profilePhotoRepository.save(photo);
    }

    public EventPhoto save(EventPhoto photo) {
        return eventPhotoRepository.save(photo);
    }

    @SuppressWarnings("unchecked")
    public <T extends Photo> Optional<T> findByPhotoId(String photoId) {
        return photoRepository.findByPhotoId(photoId);
    }

    @SuppressWarnings("unchecked")
    public <T extends Photo> Optional<T> findById(Long id) {
        return photoRepository.findById(id);
    }

}
