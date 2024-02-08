package com.roundtable.roundtable.entity.housework;

import static com.roundtable.roundtable.entity.housework.QHouseWork.houseWork;
import static com.roundtable.roundtable.entity.housework.QHouseWorkDay.houseWorkDay;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.entity.house.House;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class HouseWorkRepositoryImpl implements HouseWorkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public HouseWorkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public List<HouseWork> findHouseWorksByDayAndHouse(Day day, House house, LocalDate targetDate) {
        return queryFactory
                .select(houseWork)
                .from(houseWorkDay)
                .innerJoin(houseWork).on(houseWorkDay.houseWork.id.eq(houseWork.id))
                .where(
                        houseWorkDay.day.eq(day),
                        houseWork.house.eq(house),
                        houseWork.activeDate.loe(targetDate),
                        houseWork.deActiveDate.isNull().or(houseWork.deActiveDate.gt(targetDate))
                )
                .fetch();
    }
}
