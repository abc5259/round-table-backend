package com.roundtable.roundtable.domain.schedule;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleDayRepository extends JpaRepository<ScheduleDay, Long> {
    @Query("SELECT sd.schedule FROM ScheduleDay sd WHERE sd.day = :day")
    List<Schedule> findSchedulesByDay(@Param("day") Day day);
}
