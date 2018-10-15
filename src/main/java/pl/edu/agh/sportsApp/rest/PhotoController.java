package pl.edu.agh.sportsApp.rest;

import io.swagger.annotations.*;
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

@Api(description = "Controller for uploading, managing and downloading photos.")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/files/photo")
public class PhotoController {

    PhotoService photoService;

    @ApiOperation(value = "Return photo in high resolution.", response = Resource.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Photo returned."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
            @ApiResponse(code = 404, message = "ResponseCodes = {RESOURCE_NOT_FOUND}"),
    })
    @ResponseBody
    @GetMapping("/high/{resourceId}")
    public Resource serveHighQualityPhoto(@PathVariable String resourceId) {
        return photoService.serveHighQualityPhoto(resourceId);
    }

    @ApiOperation(value = "Return photo in low resolution.", response = Resource.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Photo returned."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
            @ApiResponse(code = 404, message = "ResponseCodes = {RESOURCE_NOT_FOUND}"),
    })
    @ResponseBody
    @GetMapping("/low/{resourceId}")
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Photo deleted."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
            @ApiResponse(code = 404, message = "ResponseCodes = {RESOURCE_NOT_FOUND}"),
    })
    @DeleteMapping("/avatar/remove")
    @ResponseBody
    public void removeUserAvatar(@ApiIgnore @AuthenticationPrincipal final User user) {
        photoService.removeProfilePhoto(user);
    }

    @ApiOperation(value = "Upload profile photo." , response = UploadResponseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Photo returned."),
            @ApiResponse(code = 400, message = "ResponseCodes = {EMPTY_FILE, INVALID_IMAGE_PROPORTIONS, FILE_TOO_BIG}"),
            @ApiResponse(code = 401, message = "Log first to gain access."),
            @ApiResponse(code = 500, message = "ResponseCodes = {MEDIA_SERVICE_NOT_AVAILABLE}")
    })
    @PostMapping("/avatar/upload")
    public UploadResponseDTO uploadUserAvatar(
            @ApiParam(value = "Image with square proportions.", required = true) @RequestBody final MultipartFile file,
            @ApiIgnore @AuthenticationPrincipal final User user) {
        return photoService.handleUserAvatarUpload(file, user);
    }

}
