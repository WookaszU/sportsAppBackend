package pl.edu.agh.sportsApp.model.photo;

import lombok.*;
import pl.edu.agh.sportsApp.model.User;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "profilePhoto")
@Getter @Setter
public class ProfilePhoto extends Photo {

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    public ProfilePhoto(String photoId, String highResolutionPath, String lowResolutionPath) {
        super(photoId, highResolutionPath, lowResolutionPath);
    }

}
