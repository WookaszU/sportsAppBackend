package pl.edu.agh.sportsApp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity(name = "photo")
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

    @Column(name = "owner_id")
    private Long ownerId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private User owner;

    public Photo(String photoId, String highResolutionPath, String lowResolutionPath) {
        this.photoId = photoId;
        this.highResolutionPath = highResolutionPath;
        this.lowResolutionPath = lowResolutionPath;
    }
}
