package com.roundtable.roundtable.domain.schedule;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleCompletionRepository extends JpaRepository<ScheduleCompletion, Long> {
    boolean existsByScheduleIdAndCompletionDate(Long scheduleId, LocalDate completionDate);

    Optional<ScheduleCompletion> findByScheduleIdAndCompletionDate(Long scheduleId, LocalDate completionDate);

    @Query("""
        select sc
        from ScheduleCompletion sc
        join fetch sc.schedule
        where sc.id = :id
    """)
    Optional<ScheduleCompletion> findWithScheduleById(@Param("id") Long id);
}
