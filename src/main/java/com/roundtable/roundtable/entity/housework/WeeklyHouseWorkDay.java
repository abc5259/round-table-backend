package com.roundtable.roundtable.entity.housework;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyHouseWorkDay {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "DAY_ID")
    private Day day;

    @ManyToOne
    @JoinColumn(name = "WEEKLY_HOUSE_WORK_ID")
    private WeeklyHouseWork weeklyHouseWork;


    public WeeklyHouseWorkDay(Day day, WeeklyHouseWork weeklyHouseWork) {
        this.day = day;
        this.weeklyHouseWork = weeklyHouseWork;
    }
}
