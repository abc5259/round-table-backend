package com.roundtable.roundtable.entity.housework;

import static com.roundtable.roundtable.entity.housework.QHouseWorkMember.houseWorkMember;
import static org.junit.jupiter.api.Assertions.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.QHouse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
@SpringBootTest
@Transactional
class HouseWorkRepositoryImplTest {

    @PersistenceContext
    EntityManager em;

    @Test
    void findAllHouse() {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
        List<HouseWorkMember> fetch = jpaQueryFactory
                .selectFrom(houseWorkMember)
//                .join(houseWorkMember.houseWork, QHouseWork.houseWork).on(houseWorkMember.sequence.eq(QHouseWork.houseWork.currSequence))
                .join(houseWorkMember.houseWork, QOneTimeHouseWork.oneTimeHouseWork._super)
                .join(houseWorkMember.houseWork, QWeeklyHouseWork.weeklyHouseWork._super)
                .where(QHouse.house.id.eq(1L))
                .fetch();

    }
}