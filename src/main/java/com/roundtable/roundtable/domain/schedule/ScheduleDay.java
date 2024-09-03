package com.roundtable.roundtable.domain.schedule;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleDay {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Schedule schedule;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Day dayOfWeek;

    @Builder
    private ScheduleDay(Schedule schedule, Day dayOfWeek) {
        this.schedule = schedule;
        this.dayOfWeek = dayOfWeek;
    }

    public static ScheduleDay create(Schedule schedule, Day day) {
        return new ScheduleDay(schedule, day);
    }
}
