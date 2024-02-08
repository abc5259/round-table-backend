package com.roundtable.roundtable.entity.housework;

import com.roundtable.roundtable.entity.house.House;
import java.time.LocalDate;
import java.util.List;

public interface HouseWorkRepositoryCustom {

    List<HouseWork> findHouseWorksByDayAndHouse(Day day, House house, LocalDate targetDate);
}
