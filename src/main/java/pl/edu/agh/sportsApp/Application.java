package pl.edu.agh.sportsApp;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import pl.edu.agh.sportsApp.app.AppStartupTasks;

@RequiredArgsConstructor
@SpringBootApplication
public class Application {

    @NonNull
    private final AppStartupTasks appStartupTasks;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String hibernateDdlAuto;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartupTasks() {
        appStartupTasks.prepareFileStorageDirectories();
        if(hibernateDdlAuto.equals("create"))
            appStartupTasks.runDatabaseScripts("postgresScripts.sql");
    }

}