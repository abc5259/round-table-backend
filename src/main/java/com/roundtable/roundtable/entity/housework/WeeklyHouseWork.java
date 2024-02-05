package com.roundtable.roundtable.entity.housework;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
public class WeeklyHouseWork extends HouseWork {
    @Enumerated(EnumType.STRING)
    @Column(name = "DAY")
    private DayOfWeek dayOfWeek;

    @Column
    private LocalTime assignedTime;

    @Enumerated(EnumType.STRING)
    private HouseWorkDivision houseWorkDivision;
}
