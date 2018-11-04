package pl.edu.agh.sportsApp.filemanager.namegenerator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Primary
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UUIDNameGenerator implements NameGenerator {

    public String generate() {
        return UUID.randomUUID().toString();
    }

}
