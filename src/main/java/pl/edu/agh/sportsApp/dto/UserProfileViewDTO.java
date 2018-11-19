package pl.edu.agh.sportsApp.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import pl.edu.agh.sportsApp.repository.event.projection.EventData;
import pl.edu.agh.sportsApp.repository.user.projection.UserData;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Getter
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileViewDTO {

    Long id;

    String firstName;

    String lastName;

    Double rating;

    String photoId;

    Integer favoriteCategory;

    Integer eventParticipationNum;

    Integer eventOwnershipsNum;

    Integer userRatingsNum;

    List<EventDataListElem> userMemories;

    public UserProfileViewDTO(UserData userData, List<EventData> eventDataList) {
        this.id = userData.getId();
        this.firstName = userData.getFirstName();
        this.lastName = userData.getLastName();
        this.rating = userData.getRating();
        this.photoId = userData.getPhotoId();
        this.favoriteCategory = userData.getFavoriteCategory();
        this.eventParticipationNum = userData.getEventParticipationNum();
        this.eventOwnershipsNum = userData.getEventOwnershipsNum();
        this.userRatingsNum = userData.getUserRatingsNum();
        this.userMemories = convertToListElem(eventDataList);
    }

    private List<EventDataListElem> convertToListElem(List<EventData> eventDataList) {
        List<EventDataListElem> userMemories = new LinkedList<>();
        for(EventData eventData: eventDataList)
            userMemories.add(EventDataListElem.builder()
                    .id(eventData.getId())
                    .content(eventData.getContent())
                    .startDate(eventData.getStartDate())
                    .categoryId(eventData.getCategoryId())
                    .build());

        return userMemories;
    }

}

@Builder
@Getter
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
class EventDataListElem {

    Long id;

    String content;

    LocalDateTime startDate;

    Long categoryId;

}
