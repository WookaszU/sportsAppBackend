package pl.edu.agh.sportsApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.edu.agh.sportsApp.model.Account;
import pl.edu.agh.sportsApp.service.AccountService;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
@PropertySource(value = {"/props/path.properties"})
public class Application {

    private final static Logger logger = Logger.getLogger(Application.class.getName());
    private static ConfigurableApplicationContext ctx;

    // ONLY FOR TESTS !!! - REMOVE BELOW FIELDS and method onlyForTestPurposes()
    @Autowired
    private AccountService accountService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    public static void main(String[] args) {
        ctx = SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartupTasks(){
        prepareFileStorageDirectories();
        onlyForTestPurposes();
    }

    private void prepareFileStorageDirectories(){
        File file = new File("files/avatars");
        if(file.exists())
            return;

        if (!file.mkdirs()) {
            logger.log(Level.SEVERE,"Could not create required directories! Closing application.");
            SpringApplication.exit(ctx);
        }
    }

    private void onlyForTestPurposes(){

        Account account = Account.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("test"))
                .firstName("test")
                .lastName("test")
                .build();

        account.setEnabled(true);
        accountService.saveAccount(account);
    }

}