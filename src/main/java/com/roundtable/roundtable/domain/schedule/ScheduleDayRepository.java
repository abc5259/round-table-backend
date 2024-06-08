package com.roundtable.roundtable.domain.schedule;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleDayRepository extends JpaRepository<ScheduleDay, Long> {
    @Query("SELECT sc FROM ScheduleDay sd join sd.schedule sc WHERE sd.day = :day AND sc.scheduleType = 'REPEAT'")
    List<Schedule> findRepeatSchedulesByDay(@Param("day") Day day);
}
