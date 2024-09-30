package com.roundtable.roundtable.domain.schedule;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleDayRepository extends JpaRepository<ScheduleDay, Long> {
    @Query("SELECT sc FROM ScheduleDay sd join sd.schedule sc WHERE sd.dayOfWeek = :dayOfWeek AND sc.scheduleType = 'REPEAT'")
    List<Schedule> findRepeatSchedulesByDay(@Param("dayOfWeek") Day dayOfWeek);

    boolean existsByScheduleIdAndDayOfWeek(Long scheduleId, Day dayOfWeek);

    Optional<ScheduleDay> findByScheduleIdAndDayOfWeek(Long scheduleId, Day dayOfWeek);
}
