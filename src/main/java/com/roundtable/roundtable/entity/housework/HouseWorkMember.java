package com.roundtable.roundtable.entity.housework;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseWorkMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOUSE_ID")
    private House house;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOUSEWORK_ID")
    private HouseWork houseWork;

    public HouseWorkMember(Integer sequence, Member member, House house,
                           HouseWork houseWork) {
        this.sequence = sequence;
        this.member = member;
        this.house = house;
        this.houseWork = houseWork;
    }
}
