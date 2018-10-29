package pl.edu.agh.sportsApp.model;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import pl.edu.agh.sportsApp.dto.EventDTO;
import pl.edu.agh.sportsApp.model.chat.EventChat;
import pl.edu.agh.sportsApp.model.photo.EventPhoto;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "event")
@Table(indexes = {
        @Index(name = "archive_index", columnList = "startDate")
})
@EqualsAndHashCode(exclude = {"id", "content", "ownerId", "owner", "participantIds", "participants"})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(0)
    private Integer categoryId;

    @NotNull
    @Latitude
    private Double latitude;

    @NotNull
    @Longitude
    private Double longitude;

    @NotNull
    private LocalDateTime startDate;

    @Column(length = 5000)
    private String content;

    @NotNull
    @Column(name = "owner_id")
    private Long ownerId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private User owner;

    @Transient
    private Set<Long> participantIds = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinTable(
            name = "UserEvents",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "users_id")}
    )
    @Builder.Default
    private Set<User> participants = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "chat_id", referencedColumnName = "id", nullable = false)
    private EventChat eventChat;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = javax.persistence.CascadeType.ALL,
            orphanRemoval = true)
    private Set<EventPhoto> eventPhotos;

    public void addEventPhoto(EventPhoto eventPhoto) {
        eventPhoto.setEvent(this);
        this.getEventPhotos().add(eventPhoto);
    }

    public void removeEventPhoto(EventPhoto eventPhoto) {
        this.getEventPhotos().remove(eventPhoto);
    }

    public EventDTO mapToDTO() {
        return EventDTO.builder()
                .id(this.getId())
                .categoryId(this.getCategoryId())
                .latitude(this.getLatitude())
                .longitude(this.getLongitude())
                .startDate(this.getStartDate())
                .content(this.getContent())
                .ownerId(this.getOwnerId())
                .participantIds(this.getParticipants().stream()
                        .map(User::getId)
                        .collect(Collectors.toList()))
                .build();
    }

}