package com.roundtable.roundtable.entity.schedule;

import static com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode.CATEGORY_NOT_SAME_HOUSE;

import com.roundtable.roundtable.entity.common.BaseEntity;
import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.global.exception.CoreException.CreateEntityException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Embedded
    private Frequency frequency;

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
    @ManyToOne
    private House house;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "schedule", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ScheduleMember> scheduleMembers = new ArrayList<>();

    @Builder
    private Schedule(Long id,
                     String name,
                     Frequency frequency,
                     LocalDate startDate,
                     LocalTime startTime,
                     Integer sequence,
                     Integer sequenceSize,
                     DivisionType divisionType,
                     House house,
                     Category category) {

        this.name = name;
        this.frequency = frequency;
        this.startDate = startDate;
        this.startTime = startTime;
        this.sequence = sequence;
        this.sequenceSize = sequenceSize;
        this.divisionType = divisionType;
        this.house = house;
        this.category = category;
        this.id = id;
    }

    public static Schedule Id(Long scheduleId) {
        return Schedule.builder().id(scheduleId).build();
    }

    public static Schedule create(
            String name,
            Frequency frequency,
            LocalDate startDate,
            LocalTime startTime,
            DivisionType divisionType,
            House house,
            int sequenceSize,
            Category category
    ) {

        if(!category.isSameHouse(house)) {
            throw new CreateEntityException(CATEGORY_NOT_SAME_HOUSE);
        }

        return Schedule.builder()
                .name(name)
                .frequency(frequency)
                .startDate(startDate)
                .startTime(startTime)
                .divisionType(divisionType)
                .house(house)
                .sequence(1)
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
}
