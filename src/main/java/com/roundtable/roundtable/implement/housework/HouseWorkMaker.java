package com.roundtable.roundtable.implement.housework;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.housework.HouseWorkRepository;
import com.roundtable.roundtable.entity.housework.OneTimeHouseWork;
import com.roundtable.roundtable.entity.housework.WeeklyHouseWork;
import com.roundtable.roundtable.entity.member.Member;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class HouseWorkMaker {

    private final HouseWorkRepository houseWorkRepository;
    private final HouseWorkMemberMaker houseWorkMemberMaker;
    private final WeeklyHouseWorkDayMaker weeklyHouseWorkDayMaker;

    public Long createOneTimeHouseWork(House house, CreateOneTimeHouseWork createOneTimeHouseWork, List<Member> assignedMembers) {
        OneTimeHouseWork oneTimeHouseWork = new OneTimeHouseWork(
                createOneTimeHouseWork.name(),
                createOneTimeHouseWork.houseWorkCategory(),
                createOneTimeHouseWork.currSequence(),
                createOneTimeHouseWork.sequenceSize(),
                createOneTimeHouseWork.assignedDate()
        );

        OneTimeHouseWork savedOneTimeHouseWork = houseWorkRepository.save(oneTimeHouseWork);

        houseWorkMemberMaker.createOneTimeHouseWorkMembers(house, savedOneTimeHouseWork, assignedMembers);

        return savedOneTimeHouseWork.getId();
    }

    public Long createWeeklyHouseWork(House house, CreateWeeklyHouseWork createWeeklyHouseWork, List<Member> assignedMembers) {
        WeeklyHouseWork weeklyHouseWork = new WeeklyHouseWork(
                createWeeklyHouseWork.name(),
                createWeeklyHouseWork.houseWorkCategory(),
                createWeeklyHouseWork.assignedTime(),
                createWeeklyHouseWork.houseWorkDivision(),
                createWeeklyHouseWork.currSequence(),
                createWeeklyHouseWork.sequenceSize()
        );

        WeeklyHouseWork savedWeeklyHouseWork = houseWorkRepository.save(weeklyHouseWork);

        houseWorkMemberMaker.createWeeklyHouseWorkMembers(house, savedWeeklyHouseWork, assignedMembers);
        weeklyHouseWorkDayMaker.createWeeklyHouseWorkDays(createWeeklyHouseWork.dayIds(), savedWeeklyHouseWork);

        return savedWeeklyHouseWork.getId();
    }
}
