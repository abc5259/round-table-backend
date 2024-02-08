package com.roundtable.roundtable.entity.housework;

import com.roundtable.roundtable.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseWork extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    protected String name;

    @Column(name = "CATEGORY",nullable = false)
    @Enumerated(EnumType.STRING)
    protected HouseWorkCategory houseWorkCategory;

    @Column(nullable = false)
    protected Integer currSequence;

    @Column(nullable = false)
    protected Integer sequenceSize;

    @Column(nullable = false)
    private LocalDate activeDate;

    @Column
    private LocalDate deActiveDate;

    @Column(nullable = false)
    private LocalTime assignedTime;


    public HouseWork(String name, HouseWorkCategory houseWorkCategory, Integer currSequence, Integer sequenceSize,
                     LocalDate activeDate, LocalDate deActiveDate, LocalTime assignedTime) {
        this.name = name;
        this.houseWorkCategory = houseWorkCategory;
        this.currSequence = currSequence;
        this.sequenceSize = sequenceSize;
        this.activeDate = activeDate;
        this.deActiveDate = deActiveDate;
        this.assignedTime = assignedTime;
    }

    public Long getId() {
        return id;
    }
}
