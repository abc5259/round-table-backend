package com.roundtable.roundtable.domain.schedule;

import java.time.DayOfWeek;
import lombok.Getter;

@Getter
public enum Day {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7)
    ;

    private final int id;

    Day(int id) {
        this.id = id;
    }

    public static Day fromId(int id) {
        return switch (id) {
            case 1 -> MONDAY;
            case 2 -> TUESDAY;
            case 3 -> WEDNESDAY;
            case 4 -> THURSDAY;
            case 5 -> FRIDAY;
            case 6 -> SATURDAY;
            case 7 -> SUNDAY;
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    public static Day forDayOfWeek(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> MONDAY;
            case TUESDAY -> TUESDAY;
            case WEDNESDAY -> WEDNESDAY;
            case THURSDAY -> THURSDAY;
            case FRIDAY -> FRIDAY;
            case SATURDAY -> SATURDAY;
            case SUNDAY -> SUNDAY;
        };
    }
}
