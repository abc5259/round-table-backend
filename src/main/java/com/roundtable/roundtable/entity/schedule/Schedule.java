package com.roundtable.roundtable.entity.schedule;

import com.roundtable.roundtable.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FrequencyType frequencyType;

    @Column(nullable = false)
    private Integer frequencyInterval;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private Integer sequence;

    @Column(nullable = false)
    private Integer sequenceSize;
}
