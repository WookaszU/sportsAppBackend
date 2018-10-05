package pl.edu.agh.sportsApp.filemanager.photoservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.sportsApp.filemanager.PhotoManager;
import pl.edu.agh.sportsApp.filemanager.exception.InvalidPhotoProportionsException;
import pl.edu.agh.sportsApp.model.Account;
import pl.edu.agh.sportsApp.model.Photo;
import pl.edu.agh.sportsApp.service.AccountService;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class UserPhotoService {

    PhotoManager photoManager;
    AccountService accountService;

    public Photo setProfilePhoto(Account account, MultipartFile file) throws InvalidPhotoProportionsException, IOException {
        removeProfilePhoto(account);
        Photo photo = photoManager.savePhoto(file);
        account.setUserPhoto(photo);
        accountService.saveAccount(account);
        return photo;
    }

    public Optional<Photo> removeProfilePhoto(Account account){
        Photo photo = account.getUserPhoto();
        if(photo == null)
            return Optional.empty();
        account.setUserPhoto(null);
        accountService.saveAccount(account);
        return photoManager.removePhoto(photo);
    }

}
