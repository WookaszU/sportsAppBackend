package pl.edu.agh.sportsApp.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.sportsApp.app.AppStartupTasks;

@Api(description = "Controller for testing purposes.")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/public/test")
public class TestController {

    @NonNull
    AppStartupTasks appStartupTasks;

    @ApiOperation(value = "Database data killer.",
            notes = "Default test-users will be in database after clean-up.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All database tables truncated."),
            @ApiResponse(code = 409, message = "Error during truncating tables.")
    })
    @DeleteMapping("/db/kill")
    public void serveHighQualityPhoto() {
        appStartupTasks.runDatabaseScripts("clearDB.sql");
    }

}
