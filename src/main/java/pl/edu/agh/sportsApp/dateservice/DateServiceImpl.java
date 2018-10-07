package pl.edu.agh.sportsApp.dateservice;

import lombok.AllArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@AllArgsConstructor
public class DateServiceImpl implements DateService {

    private ZoneId zoneId;

    @Override
    public ZonedDateTime now() {
        return ZonedDateTime.now(zoneId);
    }
}
