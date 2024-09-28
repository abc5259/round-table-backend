package com.roundtable.roundtable.domain.schedule;

import com.roundtable.roundtable.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleCompletion extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate completionDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Schedule schedule;

    @NotNull
    private Integer sequence;

    @Builder
    public ScheduleCompletion(Long id, LocalDate completionDate, Schedule schedule, Integer sequence) {
        this.id = id;
        this.completionDate = completionDate;
        this.schedule = schedule;
        this.sequence = sequence;
    }

    public static ScheduleCompletion create(Schedule schedule, LocalDate completionDate, Integer sequence) {
        return ScheduleCompletion.builder()
                .schedule(schedule)
                .completionDate(completionDate)
                .sequence(sequence)
                .build();
    }

    public String getScheduleName() {
        return schedule.getName();
    }
}
