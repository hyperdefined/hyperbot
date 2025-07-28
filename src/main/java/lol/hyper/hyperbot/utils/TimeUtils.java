package lol.hyper.hyperbot.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    public static String formatDate(long time) {
        Instant instant = Instant.ofEpochSecond(time);
        ZonedDateTime utcTime = instant.atZone(ZoneOffset.UTC);
        ZonedDateTime estTime = utcTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mm a");
        return estTime.format(formatter);
    }
}
