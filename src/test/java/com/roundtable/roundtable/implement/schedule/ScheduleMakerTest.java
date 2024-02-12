package com.roundtable.roundtable.implement.schedule;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.HouseRepository;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.member.MemberRepository;
import com.roundtable.roundtable.entity.schedule.DivisionType;
import com.roundtable.roundtable.entity.schedule.FrequencyType;
import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.entity.schedule.ScheduleException.CreateScheduleException;
import com.roundtable.roundtable.entity.schedule.ScheduleMember;
import com.roundtable.roundtable.entity.schedule.ScheduleMemberRepository;
import jakarta.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ScheduleMakerTest {
    @Autowired
    ScheduleMaker scheduleMaker;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    HouseRepository houseRepository;

    @Autowired
    ScheduleMemberRepository scheduleMemberRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        House house = House.of("name1");
        house = houseRepository.save(house);

        Member member1 = Member.builder().email("email1").password("password").build();
        Member member2 = Member.builder().email("email2").password("password").build();
        member1.enterHouse(house);
        member2.enterHouse(house);
        memberRepository.saveAll(List.of(member1, member2));
    }

    @DisplayName("스케줄에 필요한 내용과 수행할 맴버를 받아 스케줄을 생성한다.")
    @Test
    void createSchedule_FIX() {
        //given
        House house = houseRepository.findAll().get(0);
        List<Long> memberIds = memberRepository.findAll().stream().map(Member::getId).toList();

        CreateSchedule request =
                creatCreateRequest("schedule1", FrequencyType.ONCE, 0, memberIds, DivisionType.FIX, LocalDate.now());

        //when
        Schedule schedule = scheduleMaker.createSchedule(request, house);

        //then
        assertThat(schedule).isNotNull()
                .extracting("id", "name", "startDate", "startTime", "sequence", "sequenceSize")
                .contains(
                        schedule.getId(), request.name(), request.startDate(), request.startTime(), 1, memberIds.size()
                );
        assertThat(schedule.getFrequency())
                .extracting("frequencyType", "frequencyInterval")
                .contains(
                        request.frequencyType(), request.frequencyInterval()
                );
        List<ScheduleMember> scheduleMembers = scheduleMemberRepository.findAll();
        assertThat(scheduleMembers).hasSize(2)
                .extracting("sequence")
                .contains(
                        1,
                        1
                );

    }

    @DisplayName("스케줄에 필요한 내용과 수행할 맴버를 받아 스케줄을 생성한다.")
    @Test
    void createSchedule_ROTATION() {
        //given
        House house = houseRepository.findAll().get(0);
        List<Long> memberIds = memberRepository.findAll().stream().map(Member::getId).toList();

        CreateSchedule request =
                creatCreateRequest("schedule1", FrequencyType.ONCE, 0, memberIds, DivisionType.ROTATION, LocalDate.now());

        //when
        Schedule schedule = scheduleMaker.createSchedule(request, house);

        //then
        assertThat(schedule).isNotNull()
                .extracting("id", "name", "startDate", "startTime", "sequence", "sequenceSize")
                .contains(
                        schedule.getId(), request.name(), request.startDate(), request.startTime(), 1, memberIds.size()
                );
        assertThat(schedule.getFrequency())
                .extracting("frequencyType", "frequencyInterval")
                .contains(
                        request.frequencyType(), request.frequencyInterval()
                );
        List<ScheduleMember> scheduleMembers = scheduleMemberRepository.findAll();
        assertThat(scheduleMembers).hasSize(2)
                .extracting("sequence")
                .contains(
                        1,
                        2
                );

    }

    @DisplayName("스케줄을 생성할때 중복된 member id가 들어오면 에러를 던진다.")
    @Test
    void createScheduleWhenDuplicatedMemberId_fail() {
        //given
        House house = houseRepository.findAll().get(0);
        List<Long> memberIds = memberRepository.findAll().stream().map(Member::getId).toList();
        List<Long> duplicatedIds = new ArrayList<>(memberIds);
        duplicatedIds.add(memberIds.get(0));

        CreateSchedule request =
                creatCreateRequest("schedule1", FrequencyType.DAILY, 2, duplicatedIds, DivisionType.FIX, LocalDate.now());

        //when //then
        assertThatThrownBy(() -> scheduleMaker.createSchedule(request, house))
                .isInstanceOf(CreateScheduleException.class)
                .hasMessage("중복된 member id값이 있습니다.");

    }

    @DisplayName("스케줄을 생성할때는 시작날짜는 과거일 수 없다.")
    @Test
    void createScheduleWhenBeforeDate_fail() {
        //given
        House house = houseRepository.findAll().get(0);
        List<Long> memberIds = memberRepository.findAll().stream().map(Member::getId).toList();

        LocalDate startDate = LocalDate.now().minusDays(1);

        CreateSchedule request =
                creatCreateRequest("schedule1", FrequencyType.DAILY, 2, memberIds, DivisionType.FIX, startDate);

        //when //then
        assertThatThrownBy(() -> scheduleMaker.createSchedule(request, house))
                .isInstanceOf(CreateScheduleException.class)
                .hasMessage("시작날짜는 과거일 수 없습니다.");

    }

    @DisplayName("WEEKLY 스케줄을 생성할때는 시작시간은 Interval로 준 숫자에 해당하는 요일이아니라면 에러를 던진다.")
    @Test
    void createSchedule_WEEKLY_fail() {
        //given
        House house = houseRepository.findAll().get(0);
        List<Long> memberIds = memberRepository.findAll().stream().map(Member::getId).toList();

        LocalDate startDate = LocalDate.now();
        DayOfWeek day = startDate.getDayOfWeek().plus(1);

        CreateSchedule request =
                creatCreateRequest("schedule1", FrequencyType.WEEKLY, day.getValue(), memberIds, DivisionType.FIX, startDate);

        //when //then
        assertThatThrownBy(() -> scheduleMaker.createSchedule(request, house))
                .isInstanceOf(CreateScheduleException.class)
                .hasMessage("Weekly타입일땐 시작날짜는 interval로 준 값에 해당하는 요일이어야합니다.");

    }

    @DisplayName("스케줄을 생성할때는 지원하지 않는 범위의 frequency를 받으면 에러를 던진다.")
    @CsvSource({
            "ONCE, 1",
            "ONCE, -1",
            "DAILY, 0",
            "WEEKLY, 0",
            "WEEKLY, 8",
    })
    @ParameterizedTest
    void createSchedule_frequency_fail(String type, int interval) {
        //given
        House house = houseRepository.findAll().get(0);
        List<Long> memberIds = memberRepository.findAll().stream().map(Member::getId).toList();

        LocalDate startDate = LocalDate.now();
        DayOfWeek day = startDate.getDayOfWeek().plus(1);

        FrequencyType frequencyType = FrequencyType.valueOf(type);
        CreateSchedule request =
                creatCreateRequest("schedule1", frequencyType, interval, memberIds, DivisionType.FIX, startDate);

        //when //then
        assertThatThrownBy(() -> scheduleMaker.createSchedule(request, house))
                .isInstanceOf(CreateScheduleException.class)
                .hasMessage("frequencyType에 맞는 frequencyInterval값이 아닙니다.");

    }

     private CreateSchedule creatCreateRequest(String name, FrequencyType type, int interval, List<Long> memberIds,
                                               DivisionType divisitonType, LocalDate startDate) {
        return new CreateSchedule(
                name,
                type,
                interval,
                startDate,
                LocalTime.of(23,0),
                divisitonType,
                memberIds
        );
     }
}