package com.roundtable.roundtable.domain.schedule;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

}
