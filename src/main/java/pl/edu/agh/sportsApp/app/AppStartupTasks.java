package pl.edu.agh.sportsApp.app;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import pl.edu.agh.sportsApp.Application;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppStartupTasks {

    @NonNull
    DataSource dataSource;
    @NonNull
    ApplicationContext ctx;
    private final static Logger logger = Logger.getLogger(Application.class.getName());


    public void prepareFileStorageDirectories() {
        File file = new File("files/avatars");
        if (file.exists())
            return;

        if (!file.mkdirs()) {
            logger.log(Level.SEVERE, "Could not create required directories! Closing application.");
            SpringApplication.exit(ctx);
        }
    }

    private String readScriptFile(String scriptName) throws IOException {
        byte[] encoded = Files.readAllBytes(new ClassPathResource(scriptName).getFile().toPath());
        return new String(encoded);
    }

    public void runDatabaseScripts(String scriptName) {
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(true);
            Statement statement = connection.createStatement();
            statement.executeUpdate(readScriptFile(scriptName));
            statement.close();
            connection.close();
        } catch (SQLException | IOException e) {
            logger.log(Level.SEVERE, "Could not run required sql scripts! Closing application. " + e.getMessage());
            SpringApplication.exit(ctx);
        }
    }

}
