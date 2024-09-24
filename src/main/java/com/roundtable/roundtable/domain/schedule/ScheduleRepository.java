package com.roundtable.roundtable.domain.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("""
        select s
        from Schedule s
        join fetch s.scheduleMembers
        join fetch s.extraScheduleMembers
        where s.id = :id
    """)
    Schedule findByIdWithScheduleMembers(@Param("id") Long Id);
}
