package pl.edu.agh.sportsApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.model.Photo;
import pl.edu.agh.sportsApp.repository.PhotoRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoStorage {

    private PhotoRepository photoRepository;

    @Autowired
    public PhotoStorage(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public Photo save(Photo photo){
        return photoRepository.save(photo);
    }

    public void removeByPhotoId(String photoId){
        photoRepository.removePhotoByPhotoId(photoId);
    }

    public void removeById(int id){
        photoRepository.removePhotoById(id);
    }

    public Optional<Photo> findByPhotoId(String photoId){
        return photoRepository.findByPhotoId(photoId);
    }

    public Optional<Photo> findById(int id){
        return photoRepository.findById(id);
    }

}
