package pl.edu.agh.sportsApp.filemanager;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.sportsApp.config.YAMLConfig;
import pl.edu.agh.sportsApp.exceptionHandler.exceptions.InvalidPhotoProportionsException;
import pl.edu.agh.sportsApp.filemanager.namegenerator.NameGenerator;
import pl.edu.agh.sportsApp.filemanager.photoeditor.PhotoEditor;
import pl.edu.agh.sportsApp.model.Photo;
import pl.edu.agh.sportsApp.service.PhotoStorage;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PhotoManager {

    String uploadsDir;
    String HIGH_QUALITY = "High";
    String LOW_QUALITY = "Low";

    PhotoStorage photoStorage;
    NameGenerator nameGenerator;
    PhotoEditor photoEditor;
    HttpServletRequest request;

    @Autowired
    public PhotoManager(PhotoStorage photoStorage,
                        NameGenerator nameGenerator,
                        PhotoEditor photoEditor,
                        HttpServletRequest request,
                        YAMLConfig config) {
        this.photoStorage = photoStorage;
        this.nameGenerator = nameGenerator;
        this.photoEditor = photoEditor;
        this.request = request;
        this.uploadsDir = config.getMediaStorageURL();
    }

    public Optional<Resource> loadLowResolutionPhoto(String photoId) {
        Optional<Photo> photoOpt = photoStorage.findByPhotoId(photoId);
        if (!photoOpt.isPresent())
            return Optional.empty();

        return Optional.of(new PathResource(photoOpt.get().getLowResolutionPath()));
    }

    public Optional<Resource> loadHighResolutionPhoto(String resourceId) {
        Optional<Photo> photoOpt = photoStorage.findByPhotoId(resourceId);
        if (!photoOpt.isPresent())
            return Optional.empty();

        return Optional.of(new PathResource(photoOpt.get().getHighResolutionPath()));
    }

    public Photo savePhoto(MultipartFile file) throws InvalidPhotoProportionsException, IOException {

        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

        if (bufferedImage.getWidth() != bufferedImage.getHeight())
            throw new InvalidPhotoProportionsException();

        BufferedImage lowerResolutionImg = photoEditor.resize(bufferedImage);

        String fileId = nameGenerator.generate();

        String highFilePath = uploadsDir + HIGH_QUALITY + fileId + "." + file.getOriginalFilename().split("\\.")[1];   //realpath tu bylo
        String lowFilePath = uploadsDir + LOW_QUALITY + fileId + "." + file.getOriginalFilename().split("\\.")[1];   //realpath tu bylo
        File highDest = new File(highFilePath);
        File lowDest = new File(lowFilePath);

        ImageIO.write(lowerResolutionImg, "jpg", lowDest);
        file.transferTo(highDest);

        return photoStorage.save(new Photo(fileId, highFilePath, lowFilePath));
    }

    public Optional<Photo> removePhoto(String photoId) {
        Optional<Photo> photoOpt = photoStorage.findByPhotoId(photoId);
        if (!photoOpt.isPresent())
            return Optional.empty();

        return removePhoto(photoOpt.get());
    }

    public Optional<Photo> removePhoto(Photo photoToRemove) {
        if (!((new File(photoToRemove.getLowResolutionPath()).delete())
                && (new File(photoToRemove.getHighResolutionPath()).delete())))
            return Optional.empty();

        photoStorage.removeById(photoToRemove.getId());
        return Optional.of(photoToRemove);
    }

}
