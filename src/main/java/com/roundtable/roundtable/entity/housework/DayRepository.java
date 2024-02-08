package com.roundtable.roundtable.entity.housework;

import java.time.DayOfWeek;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayRepository extends JpaRepository<Day, Long> {

    Optional<Day> findByDayOfWeek(DayOfWeek day);
}
