package pl.edu.agh.sportsApp.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.LinkedList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventPhotosIdsRequestDTO {

    List<String> photoIds = new LinkedList<>();

}
