package pl.edu.agh.sportsApp.repository.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.sportsApp.model.photo.Photo;

import java.util.Optional;

public interface PhotoRepository<T extends Photo> extends JpaRepository<T, Long> {

    Optional<T> findByPhotoId(String photoId);

    void deleteByPhotoId(String photoId);

}
