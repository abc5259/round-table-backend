package com.roundtable.roundtable.entity.housework;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.DayOfWeek;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Day {
    @Id @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;


}
