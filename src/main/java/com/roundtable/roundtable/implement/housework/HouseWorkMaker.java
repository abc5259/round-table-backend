package com.roundtable.roundtable.implement.housework;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.housework.HouseWorkRepository;
import com.roundtable.roundtable.entity.housework.OneTimeHouseWork;
import com.roundtable.roundtable.entity.housework.WeeklyHouseWork;
import com.roundtable.roundtable.entity.member.Member;
import jakarta.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class HouseWorkMaker {

    private final HouseWorkRepository houseWorkRepository;
    private final HouseWorkMemberMaker houseWorkMemberMaker;
    private final HouseWorkDayMaker houseWorkDayMaker;

    public Long createOneTimeHouseWork(House house, CreateOneTimeHouseWork createOneTimeHouseWork, List<Member> assignedMembers) {

        OneTimeHouseWork oneTimeHouseWork = new OneTimeHouseWork(
                createOneTimeHouseWork.name(),
                createOneTimeHouseWork.houseWorkCategory(),
                createOneTimeHouseWork.currSequence(),
                createOneTimeHouseWork.sequenceSize(),
                createOneTimeHouseWork.assignedDate().toLocalDate(),
                createOneTimeHouseWork.assignedDate().toLocalDate().plusDays(1),
                createOneTimeHouseWork.assignedDate().toLocalTime(),
                house
        );

        OneTimeHouseWork savedOneTimeHouseWork = houseWorkRepository.save(oneTimeHouseWork);

        houseWorkMemberMaker.createOneTimeHouseWorkMembers(savedOneTimeHouseWork, assignedMembers);
        houseWorkDayMaker.createHouseWorkDay(createOneTimeHouseWork.assignedDate().getDayOfWeek(), savedOneTimeHouseWork);

        return savedOneTimeHouseWork.getId();
    }

    public Long createWeeklyHouseWork(House house, CreateWeeklyHouseWork createWeeklyHouseWork, List<Member> assignedMembers) {
        WeeklyHouseWork weeklyHouseWork = new WeeklyHouseWork(
                createWeeklyHouseWork.name(),
                createWeeklyHouseWork.houseWorkCategory(),
                createWeeklyHouseWork.currSequence(),
                createWeeklyHouseWork.sequenceSize(),
                createWeeklyHouseWork.activeDate(),
                null,
                createWeeklyHouseWork.assignedTime(),
                house,
                createWeeklyHouseWork.houseWorkDivision()
        );

        WeeklyHouseWork savedWeeklyHouseWork = houseWorkRepository.save(weeklyHouseWork);

        houseWorkMemberMaker.createWeeklyHouseWorkMembers(savedWeeklyHouseWork, assignedMembers);
        houseWorkDayMaker.createHouseWorkDays(createWeeklyHouseWork.dayIds(), savedWeeklyHouseWork);

        return savedWeeklyHouseWork.getId();
    }
}
