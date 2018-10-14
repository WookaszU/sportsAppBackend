package pl.edu.agh.sportsApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.sportsApp.model.Photo;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<Photo> findByPhotoId(String photoId);
}
