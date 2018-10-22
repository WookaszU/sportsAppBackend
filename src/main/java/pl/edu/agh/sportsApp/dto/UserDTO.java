package pl.edu.agh.sportsApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String photoId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Long> eventOwnedIds = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Long> eventParticipantIds = new ArrayList<>();
}
