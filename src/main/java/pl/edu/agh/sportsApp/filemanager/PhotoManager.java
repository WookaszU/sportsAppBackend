package pl.edu.agh.sportsApp.filemanager;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.sportsApp.config.YAMLConfig;
import pl.edu.agh.sportsApp.dto.ResponseCode;
import pl.edu.agh.sportsApp.exceptionHandler.exceptions.PhotoProcessingException;
import pl.edu.agh.sportsApp.filemanager.namegenerator.NameGenerator;
import pl.edu.agh.sportsApp.filemanager.photoeditor.PhotoEditor;
import pl.edu.agh.sportsApp.model.photo.EventPhoto;
import pl.edu.agh.sportsApp.model.photo.Photo;
import pl.edu.agh.sportsApp.model.photo.ProfilePhoto;
import pl.edu.agh.sportsApp.service.PhotoStorage;

import javax.imageio.ImageIO;
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

    @Autowired
    public PhotoManager(PhotoStorage photoStorage,
                        NameGenerator nameGenerator,
                        PhotoEditor photoEditor,
                        YAMLConfig config) {
        this.photoStorage = photoStorage;
        this.nameGenerator = nameGenerator;
        this.photoEditor = photoEditor;
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

    private PhotoPaths savePhotoToServerStorage(BufferedImage originalImage, String photoFormat) throws IOException {

        BufferedImage lowerResolutionImg = photoEditor.resize(originalImage);
        String fileId = nameGenerator.generate();

        String highFilePath = uploadsDir + HIGH_QUALITY + fileId + "." + photoFormat;
        String lowFilePath = uploadsDir + LOW_QUALITY + fileId + "." + photoFormat;
        File highDest = new File(highFilePath);
        File lowDest = new File(lowFilePath);

        ImageIO.write(lowerResolutionImg, photoFormat, lowDest);
        ImageIO.write(originalImage, photoFormat, highDest);

        return new PhotoPaths(fileId, highFilePath, lowFilePath);
    }

    @SuppressWarnings("all")
    public ProfilePhoto saveProfilePhoto(MultipartFile file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if (bufferedImage.getWidth() != bufferedImage.getHeight())
            throw new PhotoProcessingException(ResponseCode.INVALID_IMAGE_PROPORTIONS.name());

        PhotoPaths photoPaths =
                savePhotoToServerStorage(bufferedImage, file.getOriginalFilename().split("\\.")[1]);

        return new ProfilePhoto(
                photoPaths.getFileId(),
                photoPaths.getHighFilePath(),
                photoPaths.getLowFilePath());
    }

    @SuppressWarnings("all")
    public EventPhoto saveEventPhoto(MultipartFile file) throws IOException {
        PhotoPaths photoPaths = savePhotoToServerStorage(ImageIO.read(file.getInputStream()),
                file.getOriginalFilename().split("\\.")[1]);

        return new EventPhoto(
                photoPaths.getFileId(),
                photoPaths.getHighFilePath(),
                photoPaths.getLowFilePath());
    }

    public boolean removePhotoFromServerStorage(Photo photoToRemove) {
        return (new File(photoToRemove.getLowResolutionPath()).delete())
                && (new File(photoToRemove.getHighResolutionPath()).delete());
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    private class PhotoPaths {
        String fileId;
        String highFilePath;
        String lowFilePath;
    }
}
