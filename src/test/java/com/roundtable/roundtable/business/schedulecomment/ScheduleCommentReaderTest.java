package com.roundtable.roundtable.business.schedulecomment;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.common.CursorBasedRequest;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import com.roundtable.roundtable.domain.category.Category;
import com.roundtable.roundtable.domain.category.CategoryRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Frequency;
import com.roundtable.roundtable.domain.schedule.FrequencyType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedulecomment.Content;
import com.roundtable.roundtable.domain.schedulecomment.ScheduleComment;
import com.roundtable.roundtable.domain.schedulecomment.ScheduleCommentRepository;
import com.roundtable.roundtable.domain.schedulecomment.dto.MemberDetailDto;
import com.roundtable.roundtable.domain.schedulecomment.dto.ScheduleCommentDetailDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ScheduleCommentReaderTest extends IntegrationTestSupport {

    @Autowired
    private ScheduleCommentReader scheduleCommentReader;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ScheduleCommentRepository scheduleCommentRepository;

    @DisplayName("스케쥴 id 기반으로 댓글을 조회할 수 있다.")
    @Test
    void findByScheduleId() {
        //given
        House house = appendHouse();
        Member member = appendMember(house);
        Category category = appendCategory(house);
        Schedule schedule = appendSchedule(category, house);
        ScheduleComment scheduleComment1 = appendScheduleComment(schedule, member, "content1");
        ScheduleComment scheduleComment2 = appendScheduleComment(schedule, member, "content2");
        ScheduleComment scheduleComment3 = appendScheduleComment(schedule, member, "content3");
        ScheduleComment scheduleComment4 = appendScheduleComment(schedule, member, "content4");

        CursorBasedRequest cursorBasedRequest = new CursorBasedRequest(0L, 4);
        //when
        CursorBasedResponse<List<ScheduleCommentDetailDto>> scheduleCommentResponse = scheduleCommentReader.findByScheduleId(
                schedule.getId(), cursorBasedRequest);

        //then
        assertThat(scheduleCommentResponse.getLastCursorId()).isEqualTo(scheduleComment4.getId());
        assertThat(scheduleCommentResponse.getContent())
                .extracting("commentId", "content", "writer")
                .containsExactly(
                        Tuple.tuple(scheduleComment1.getId(), scheduleComment1.getContent().getContent(), new MemberDetailDto(member.getId(), member.getName())),
                        Tuple.tuple(scheduleComment2.getId(), scheduleComment2.getContent().getContent(), new MemberDetailDto(member.getId(), member.getName())),
                        Tuple.tuple(scheduleComment3.getId(), scheduleComment3.getContent().getContent(), new MemberDetailDto(member.getId(), member.getName())),
                        Tuple.tuple(scheduleComment4.getId(), scheduleComment4.getContent().getContent(), new MemberDetailDto(member.getId(), member.getName()))
                );


     }

    private Member appendMember(House house) {
        Member member = Member.builder().email("email").password("password").house(house).build();
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
                .frequency(Frequency.builder().frequencyType(FrequencyType.DAILY).frequencyInterval(2).build())
                .sequence(1)
                .sequenceSize(1)
                .house(house)
                .divisionType(DivisionType.FIX)
                .build();
        return scheduleRepository.save(schedule);
    }

    private Category appendCategory(House house) {
        Category category = Category.builder().house(house).name("name").point(1).build();
        return categoryRepository.save(category);
    }

    private ScheduleComment appendScheduleComment(Schedule schedule, Member member, String content) {
        ScheduleComment scheduleComment = ScheduleComment.builder().schedule(schedule).writer(member)
                .content(Content.builder().content(content).build()).build();
        return scheduleCommentRepository.save(scheduleComment);
    }
}