package com.roundtable.roundtable.entity.housework;

import java.time.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayRepository extends JpaRepository<Day, DayOfWeek> {
}
