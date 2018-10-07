package pl.edu.agh.sportsApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
@PropertySource(value = {"/props/path.properties"})
public class Application {

    private final static Logger logger = Logger.getLogger(Application.class.getName());
    private static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        ctx = SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void prepareFileStorageDirectories(){
        File file = new File("files/avatars");
        if(file.exists())
            return;

        if (!file.mkdirs()) {
            logger.log(Level.SEVERE,"Could not create required directories! Closing application.");
            SpringApplication.exit(ctx);
        }
    }

}