package pl.edu.agh.sportsApp.websocket.principal;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.security.auth.Subject;
import java.security.Principal;

@Builder
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class SocketPrincipal implements Principal {

    String idAsString;

    @Getter
    Long id;

    @Getter
    String firstName;

    @Getter
    String lastName;

    @Getter
    String photoId;

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

    public String getName() {
        return idAsString;
    }

}
