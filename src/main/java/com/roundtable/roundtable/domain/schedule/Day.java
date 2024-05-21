package com.roundtable.roundtable.domain.schedule;

import com.roundtable.roundtable.global.exception.CoreException;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.DayOfWeek;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Day {
    public static final int MONDAY_ID = 1;
    public static final int TUESDAY_ID = 2;
    public static final int WEDNESDAY_ID = 3;
    public static final int THURSDAY_ID = 4;
    public static final int FRIDAY_ID = 5;
    public static final int SATURDAY_ID = 6;
    public static final int SUNDAY_ID = 7;
    public static final Day MONDAY = new Day(MONDAY_ID, "MONDAY");
    public static final Day TUESDAY = new Day(TUESDAY_ID, "TUESDAY");
    public static final Day WEDNESDAY = new Day(WEDNESDAY_ID, "WEDNESDAY");
    public static final Day THURSDAY = new Day(THURSDAY_ID, "THURSDAY");
    public static final Day FRIDAY = new Day(FRIDAY_ID, "FRIDAY");
    public static final Day SATURDAY = new Day(SATURDAY_ID, "SATURDAY");
    public static final Day SUNDAY = new Day(SUNDAY_ID, "SUNDAY");

    @Id
    private Integer id;

    private String dayOfWeek;

    private Day(Integer id, String dayOfWeek) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
    }

    public static Day forId(int id) {
        return switch (id) {
            case 1 -> MONDAY;
            case 2 -> TUESDAY;
            case 3 -> WEDNESDAY;
            case 4 -> THURSDAY;
            case 5 -> FRIDAY;
            case 6 -> SATURDAY;
            case 7 -> SUNDAY;
            default -> throw new NotFoundEntityException();
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
