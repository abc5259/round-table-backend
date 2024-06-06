package com.roundtable.roundtable.domain.schedulecomment;

import static org.assertj.core.groups.Tuple.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.common.CursorPagination;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import com.roundtable.roundtable.domain.schedulecomment.dto.MemberDetailDto;
import com.roundtable.roundtable.domain.schedulecomment.dto.ScheduleCommentDetailDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ScheduleCommentQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ScheduleCommentQueryRepository scheduleCommentQueryRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleCommentRepository scheduleCommentRepository;

    @DisplayName("스케줄에 달린 댓글들을 조회할 수 있다.")
    @Test
    void findBySchedule() {
        //given
        House house = appendHouse();
        Member member1 = appendMember(house, "member1", "email1");
        Member member2 = appendMember(house, "member2", "email2");
        Member member3 = appendMember(house, "member3", "email3");
        Member member4 = appendMember(house, "member4", "email4");
        Category category = Category.CLEANING;
        Schedule schedule = appendSchedule(category, house);
        ScheduleComment scheduleComment1 = appendScheduleComment(schedule, member1);
        ScheduleComment scheduleComment2 = appendScheduleComment(schedule, member2);
        ScheduleComment scheduleComment3 = appendScheduleComment(schedule, member3);
        ScheduleComment scheduleComment4 = appendScheduleComment(schedule, member4);

        CursorPagination cursorPagination = new CursorPagination(0L, 10);
        //when
        List<ScheduleCommentDetailDto> scheduleCommentDetailDtos = scheduleCommentQueryRepository
                .findBySchedule(schedule.getId(), cursorPagination);

        //then
        Assertions.assertThat(scheduleCommentDetailDtos).hasSize(4)
                .extracting("commentId", "content", "writer")
                .containsExactly(
                        tuple(scheduleComment1.getId(), scheduleComment1.getContent().getContent(), new MemberDetailDto(member1.getId(), member1.getName())),
                        tuple(scheduleComment2.getId(), scheduleComment2.getContent().getContent(), new MemberDetailDto(member2.getId(), member2.getName())),
                        tuple(scheduleComment3.getId(), scheduleComment3.getContent().getContent(), new MemberDetailDto(member3.getId(), member3.getName())),
                        tuple(scheduleComment4.getId(), scheduleComment4.getContent().getContent(), new MemberDetailDto(member4.getId(), member4.getName()))
                );

     }

    @DisplayName("스케줄에 달린 댓글들을 커서 기반 페이지네이션으로 조회할 수 있다.")
    @Test
    void findBySchedule_cursor() {
        //given
        House house = appendHouse();
        Member member1 = appendMember(house, "member1", "email1");
        Member member2 = appendMember(house, "member2", "email2");
        Member member3 = appendMember(house, "member3", "email3");
        Member member4 = appendMember(house, "member4", "email4");
        Category category = Category.CLEANING;
        Schedule schedule = appendSchedule(category, house);
        ScheduleComment scheduleComment1 = appendScheduleComment(schedule, member1);
        ScheduleComment scheduleComment2 = appendScheduleComment(schedule, member2);
        ScheduleComment scheduleComment3 = appendScheduleComment(schedule, member3);
        ScheduleComment scheduleComment4 = appendScheduleComment(schedule, member4);

        CursorPagination cursorPagination = new CursorPagination(scheduleComment1.getId(), 2);

        //when
        List<ScheduleCommentDetailDto> scheduleCommentDetailDtos = scheduleCommentQueryRepository
                .findBySchedule(schedule.getId(), cursorPagination);

        //then
        Assertions.assertThat(scheduleCommentDetailDtos).hasSize(2)
                .extracting("commentId", "content", "writer")
                .containsExactly(
                        tuple(scheduleComment2.getId(), scheduleComment2.getContent().getContent(), new MemberDetailDto(member2.getId(), member2.getName())),
                        tuple(scheduleComment3.getId(), scheduleComment3.getContent().getContent(), new MemberDetailDto(member3.getId(), member3.getName()))
                );

    }

    private ScheduleComment appendScheduleComment(Schedule schedule, Member member) {
        ScheduleComment scheduleComment = ScheduleComment.builder().content(Content.builder().content("content1").build())
                .schedule(schedule).writer(member).build();
        return scheduleCommentRepository.save(scheduleComment);
    }


    private Member appendMember(House house, String name, String email) {
        Member member = Member.builder().name(name).email(email).password("password").house(house).build();
        return memberRepository.save(member);
    }

    private House appendHouse() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        houseRepository.save(house);
        return house;
    }

    private Schedule appendSchedule(Category category, House house) {
        Schedule schedule = Schedule.builder()
                .name("schedule")
                .category(category)
                .startDate(LocalDate.now())
                .startTime(LocalTime.MAX)
                .sequence(1)
                .sequenceSize(1)
                .house(house)
                .divisionType(DivisionType.FIX)
                .scheduleType(ScheduleType.REPEAT)
                .build();
        return scheduleRepository.save(schedule);
    }

}