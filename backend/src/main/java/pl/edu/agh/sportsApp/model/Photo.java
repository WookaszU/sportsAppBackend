package pl.edu.agh.sportsApp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor
@Entity(name = "photo")
@Data
public class Photo {

    @Column
    @Id
    @GeneratedValue
    private int id;

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
