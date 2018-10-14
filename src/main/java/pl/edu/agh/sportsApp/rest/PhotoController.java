package pl.edu.agh.sportsApp.rest;

import io.swagger.annotations.ApiOperation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.sportsApp.dto.UploadResponseDTO;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.service.PhotoService;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/files/photo")
public class PhotoController {

    PhotoService photoService;

    @ApiOperation(value = "Return photo in high resolution.")
    @GetMapping("/high/{resourceId}")
    @ResponseBody
    public Resource serveHighQualityPhoto(@PathVariable String resourceId) {
        return photoService.serveHighQualityPhoto(resourceId);
    }

    @ApiOperation(value = "Return photo in low resolution.")
    @GetMapping("/low/{resourceId}")
    @ResponseBody
    public Resource serveLowQualityPhoto(@PathVariable String resourceId) {
        return photoService.serveLowQualityPhoto(resourceId);
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

    @ApiOperation(value = "Delete user profile photo.")
    @DeleteMapping("/avatar/remove")
    @ResponseBody
    public void removeUserAvatar(@ApiIgnore @AuthenticationPrincipal final User user) {
        photoService.removeProfilePhoto(user);
    }

    @ApiOperation(value = "Upload profile photo.")
    @PostMapping("/avatar/upload")
    public UploadResponseDTO uploadUserAvatar(@RequestBody final MultipartFile file,
                                              @ApiIgnore @AuthenticationPrincipal final User user) {
        return photoService.handleUserAvatarUpload(file, user);
    }

}
