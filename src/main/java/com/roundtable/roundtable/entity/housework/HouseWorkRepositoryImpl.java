package com.roundtable.roundtable.entity.housework;

import static com.roundtable.roundtable.entity.housework.QHouseWorkMember.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.QHouse;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class HouseWorkRepositoryImpl implements CustomHouseWorkMemberRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public HouseWorkRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    public List<HouseWorkMember> findAll(House house) {

        return jpaQueryFactory
                .selectFrom(houseWorkMember)
                .join(houseWorkMember.house, QHouse.house).on(houseWorkMember.house.id.eq(house.getId()))
                .join(houseWorkMember.houseWork, QHouseWork.houseWork)
                .fetch();

    }
}
