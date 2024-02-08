package com.roundtable.roundtable.entity.housework;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseWorkDay {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Day day;

    @ManyToOne
    @JoinColumn
    private HouseWork houseWork;


    public HouseWorkDay(Day day, HouseWork houseWork) {
        this.day = day;
        this.houseWork = houseWork;
    }
}
