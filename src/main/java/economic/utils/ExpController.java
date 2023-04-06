package economic.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;

public class ExpController {
    Instant start;


    long id;

    public ExpController(Instant start, long id) {
        this.start = start;
        this.id = id;
    }

    public int countFinalExp(){
        System.out.println(start);

        Duration duration = Duration.between(start, Instant.now());
        return (int)duration.toSeconds();
    }
}
