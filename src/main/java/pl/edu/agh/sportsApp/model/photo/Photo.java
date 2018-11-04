package pl.edu.agh.sportsApp.model.photo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity(name = "photo")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Photo {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String photoId;

    @Column
    private String highResolutionPath;

    @Column
    private String lowResolutionPath;

    public Photo(String photoId, String highResolutionPath, String lowResolutionPath) {
        this.photoId = photoId;
        this.highResolutionPath = highResolutionPath;
        this.lowResolutionPath = lowResolutionPath;
    }

}
