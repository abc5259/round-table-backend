package com.roundtable.roundtable.entity.housework;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyHouseWork extends HouseWork {

    @Enumerated(EnumType.STRING)
    private HouseWorkDivision houseWorkDivision;

    public WeeklyHouseWork(String name, HouseWorkCategory houseWorkCategory, Integer currSequence, Integer sequenceSize,
                           LocalDate activeDate, LocalDate deActiveDate, LocalTime assignedTime,
                           HouseWorkDivision houseWorkDivision) {
        super(name, houseWorkCategory, currSequence, sequenceSize, activeDate, deActiveDate, assignedTime);
        this.houseWorkDivision = houseWorkDivision;
    }
}
