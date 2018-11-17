package pl.edu.agh.sportsApp.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadResponseDTO {

    ResourceType type;

    String resourceId;

}
