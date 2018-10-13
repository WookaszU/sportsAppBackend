package pl.edu.agh.sportsApp.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageDTO {

    @NotNull
    Long senderId;

    @NotNull
    String content;

    @NotNull
    String type;

}
