package com.roundtable.roundtable.domain.schedule;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleCompletionRepository extends JpaRepository<ScheduleCompletion, Long> {
    boolean existsByScheduleIdAndCompletionDate(Long scheduleId, LocalDate completionDate);
}
