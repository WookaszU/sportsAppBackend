package pl.edu.agh.sportsApp;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import pl.edu.agh.sportsApp.app.AppStartupTasks;


@SpringBootApplication
@RequiredArgsConstructor
public class Application {

    private final AppStartupTasks appStartupTasks;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartupTasks() {
        appStartupTasks.prepareFileStorageDirectories();
        appStartupTasks.runDatabaseScripts("postgresScripts.sql");
    }

}