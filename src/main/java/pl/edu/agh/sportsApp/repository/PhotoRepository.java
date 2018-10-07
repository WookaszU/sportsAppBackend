package pl.edu.agh.sportsApp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.sportsApp.model.Photo;

import java.util.Optional;

@Repository
@Transactional
public interface PhotoRepository extends CrudRepository<Photo, Integer> {

    Optional<Photo> findById(Integer id);

    Optional<Photo> findByPhotoId(String photoId);

    void removePhotoById(Integer id);

    void removePhotoByPhotoId(String photoId);

}
