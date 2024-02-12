package com.roundtable.roundtable.entity.schedule;

import com.roundtable.roundtable.entity.BaseEntity;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @Column(nullable = false)
    private String name;

    @Embedded
    private Frequency frequency;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private Integer sequence;

    @Column(nullable = false)
    private Integer sequenceSize;

    @Enumerated
    private DivisionType divisionType;

    @ManyToOne
    private House house;

    @OneToMany(mappedBy = "schedule", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ScheduleMember> scheduleMembers = new ArrayList<>();

    @Builder
    private Schedule(String name,
                     Frequency frequency,
                     LocalDate startDate,
                     LocalTime startTime,
                     Integer sequence,
                     Integer sequenceSize,
                     DivisionType divisionType,
                     House house,
                     List<ScheduleMember> scheduleMembers) {
        this.name = name;
        this.frequency = frequency;
        this.startDate = startDate;
        this.startTime = startTime;
        this.sequence = sequence;
        this.sequenceSize = sequenceSize;
        this.divisionType = divisionType;
        this.house = house;
        this.scheduleMembers = scheduleMembers;
    }

    public static Schedule create(
            String name,
            Frequency frequency,
            LocalDate startDate,
            LocalTime startTime,
            DivisionType divisionType,
            House house,
            List<ScheduleMember> scheduleMembers
    ) {

        return Schedule.builder()
                .name(name)
                .frequency(frequency)
                .startDate(startDate)
                .startTime(startTime)
                .divisionType(divisionType)
                .house(house)
                .scheduleMembers(scheduleMembers)
                .sequence(1)
                .sequenceSize(scheduleMembers.size())
                .build();
    }
}
