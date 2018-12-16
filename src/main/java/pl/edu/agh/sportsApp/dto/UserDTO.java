package pl.edu.agh.sportsApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    @NotNull
    @Email
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Double rating;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String photoId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Long> currentEventParticipantIds = new LinkedList<>();
}
