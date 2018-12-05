package pl.edu.agh.sportsApp.dto.socket;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageDTO {

    @NotNull
    String content;

}
