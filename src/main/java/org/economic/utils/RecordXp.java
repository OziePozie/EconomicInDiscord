package org.economic.utils;

import java.time.Duration;
import java.time.Instant;

public class RecordXp {
    Instant start;

    long id;

    public RecordXp(Instant start, long id) {
        this.start = start;
        this.id = id;
    }

    public int countFinalExp(){
        System.out.println(start);
        Duration duration = Duration.between(start, Instant.now());
        return (int) duration.toSeconds();
    }
}
