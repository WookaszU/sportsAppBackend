package pl.edu.agh.sportsApp.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRatingDTO {

    @NotNull
    Long userId;

    @Min(value = 1, message = "Rate must be integer between 1 and 5.")
    @Max(value = 5, message = "Rate must be integer between 1 and 5.")
    Integer rating;

}
