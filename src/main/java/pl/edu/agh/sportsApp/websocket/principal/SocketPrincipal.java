package pl.edu.agh.sportsApp.websocket.principal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.security.auth.Subject;
import java.security.Principal;

@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class SocketPrincipal implements Principal {

    String name;
    @Getter
    Long id;

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }
}
