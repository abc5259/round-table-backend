package com.roundtable.roundtable.entity.housework;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyHouseWork extends HouseWork {

    @Column
    private LocalTime assignedTime;

    @Enumerated(EnumType.STRING)
    private HouseWorkDivision houseWorkDivision;

    public WeeklyHouseWork(String name,
                           HouseWorkCategory houseWorkCategory,
                           LocalTime assignedTime,
                           HouseWorkDivision houseWorkDivision,
                           Integer currSequence,
                           Integer sequenceSize
    ) {
        super(name, houseWorkCategory, currSequence, sequenceSize);
        this.assignedTime = assignedTime;
        this.houseWorkDivision = houseWorkDivision;
    }
}
