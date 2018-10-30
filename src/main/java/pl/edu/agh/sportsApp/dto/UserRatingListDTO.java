package pl.edu.agh.sportsApp.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pl.edu.agh.sportsApp.repository.user.projection.UserRatingData;

import java.util.LinkedList;
import java.util.List;

@Builder
@Data
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class UserRatingListDTO {

    List<UserRatingsListElem> userRatings = new LinkedList<>();

    public UserRatingListDTO(List<UserRatingData> ratingDataList) {
        for(UserRatingData ratingData : ratingDataList) {
            userRatings.add(UserRatingsListElem.builder()
                    .evaluativeId(ratingData.getEvaluativeId())
                    .evaluativeFirstName(ratingData.getEvaluativeFirstName())
                    .evaluativeLastName(ratingData.getEvaluativeLastName())
                    .eventId(ratingData.getEventId())
                    .rating(ratingData.getRating())
                    .description(ratingData.getDescription())
                    .build());
        }
    }
}

@Builder
@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
class UserRatingsListElem {

    Long evaluativeId;

    String evaluativeFirstName;

    String evaluativeLastName;

    Long eventId;

    Double rating;

    String description;

}
