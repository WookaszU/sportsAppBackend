package pl.edu.agh.sportsApp.dateservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.TimeZone;

@Configuration
public class DateServiceConfig {

    @Bean
    DateService dataService(){
        return new DateServiceImpl(defaultTimeZone());
    }

    @Bean
    ZoneId defaultTimeZone(){
        return TimeZone.getTimeZone("UTC").toZoneId();
    }

}
