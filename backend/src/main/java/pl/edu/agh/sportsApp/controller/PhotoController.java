package pl.edu.agh.sportsApp.controller;

import io.swagger.annotations.ApiOperation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.sportsApp.controller.dto.ResponseCode;
import pl.edu.agh.sportsApp.filemanager.PhotoManager;
import pl.edu.agh.sportsApp.filemanager.photoservice.UserPhotoService;
import pl.edu.agh.sportsApp.filemanager.exception.InvalidPhotoProportionsException;
import pl.edu.agh.sportsApp.model.Account;
import pl.edu.agh.sportsApp.model.Photo;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/files/photo")
public class PhotoController {

    PhotoManager photoManager;
    UserPhotoService userPhotoService;

    @ApiOperation(value="Return photo in high resolution.")
    @GetMapping("/high/{resourceId}")
    @ResponseBody
    public ResponseEntity<Resource> serveHighQualityPhoto(@PathVariable String resourceId) {

        Optional<Resource> resourceOpt = photoManager.loadHighResolutionPhoto(resourceId);

        if(!resourceOpt.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Resource file = resourceOpt.get();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @ApiOperation(value="Return photo in low resolution.")
    @GetMapping("/low/{resourceId}")
    @ResponseBody
    public ResponseEntity<Resource> serveLowQualityPhoto(@PathVariable String resourceId) {

        Optional<Resource> resourceOpt = photoManager.loadLowResolutionPhoto(resourceId);

        if(!resourceOpt.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Resource file = resourceOpt.get();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

//    // TODO   remove and add photo to EVENT
//    @ApiOperation(value="Delete a photo resource.")
//    @DeleteMapping("/remove/{resourceId}")
//    @ResponseBody
//    public ResponseEntity removePhoto(@PathVariable String resourceId) {
//        if(photoManager.removePhoto(resourceId).isPresent())
//            return ResponseEntity.ok().build();
//        return ResponseEntity.notFound().build();
//    }

    @ApiOperation(value="Delete user profile photo.")
    @DeleteMapping("/avatar/remove")
    @ResponseBody
    public ResponseEntity removeUserAvatar(@ApiIgnore @AuthenticationPrincipal final Account account) {
        if(userPhotoService.removeProfilePhoto(account).isPresent())
            return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value="Upload profile photo.")
    @PostMapping("/avatar/upload")
    public ResponseEntity uploadUserAvatar(@RequestBody MultipartFile file,
                                           @ApiIgnore @AuthenticationPrincipal final Account account) {

        if(file == null || file.isEmpty())
            return new ResponseEntity<>(ResponseCode.EMPTY_FILE, HttpStatus.BAD_REQUEST);

        if(file.getSize() > 1048576)
            return new ResponseEntity<>(ResponseCode.FILE_TOO_BIG, HttpStatus.BAD_REQUEST);

        Photo photo;

        try {
            photo = userPhotoService.setProfilePhoto(account, file);
        } catch (InvalidPhotoProportionsException e) {
            return new ResponseEntity<>(ResponseCode.INVALID_IMAGE_PROPORTIONS, HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(photo.getPhotoId(), HttpStatus.OK);
    }

}
