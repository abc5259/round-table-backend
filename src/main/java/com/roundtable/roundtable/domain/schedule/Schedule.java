package com.roundtable.roundtable.domain.schedule;

import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {

    public static final int DEFAULT_SEQUENCE = 0;

    public static final int START_SEQUENCE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private Integer sequence;

    @NotNull
    private Integer sequenceSize;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DivisionType divisionType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private House house;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "schedule", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ScheduleMember> scheduleMembers = new ArrayList<>();

    @Builder
    private Schedule(Long id,
                     String name,
                     LocalDate startDate,
                     LocalTime startTime,
                     Integer sequence,
                     Integer sequenceSize,
                     DivisionType divisionType,
                     ScheduleType scheduleType,
                     House house,
                     Category category) {

        this.name = name;
        this.startDate = startDate;
        this.startTime = startTime;
        this.sequence = sequence;
        this.sequenceSize = sequenceSize;
        this.divisionType = divisionType;
        this.scheduleType = scheduleType;
        this.house = house;
        this.category = category;
        this.id = id;
    }

    public static Schedule Id(Long scheduleId) {
        return Schedule.builder().id(scheduleId).build();
    }

    public static Schedule create(
            String name,
            LocalDate startDate,
            LocalTime startTime,
            DivisionType divisionType,
            ScheduleType scheduleType,
            House house,
            int sequenceSize,
            Category category,
            LocalDate currDate
    ) {

        //분담방식이 FIX 라면 sequence 크기는 최대 1
        if(divisionType == DivisionType.FIX) {
            sequenceSize = 1;
        }

        return Schedule.builder()
                .name(name)
                .startDate(startDate)
                .startTime(startTime)
                .divisionType(divisionType)
                .scheduleType(scheduleType)
                .house(house)
                .sequence(startDate.isAfter(currDate) ? DEFAULT_SEQUENCE : START_SEQUENCE)
                .sequenceSize(sequenceSize)
                .category(category)
                .build();
    }

    public void addScheduleMembers(List<ScheduleMember> scheduleMembers) {
        for (ScheduleMember scheduleMember : scheduleMembers) {
            if(!this.scheduleMembers.contains(scheduleMember)) {
                this.scheduleMembers.add(scheduleMember);
            }
        }
    }

    public boolean isEqualSequence(Integer sequence) {
        return this.sequence.equals(sequence);
    }

    public boolean isSameHouse(Member member) {
        return member.isSameHouse(house);
    }

    public void increaseSequence() {
        sequence += 1;
        if(sequence > sequenceSize) {
            sequence = START_SEQUENCE;
        }
    }
}
