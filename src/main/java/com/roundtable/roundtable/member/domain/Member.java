package com.roundtable.roundtable.member.domain;

import com.roundtable.roundtable.global.domain.BaseEntity;
import com.roundtable.roundtable.house.domain.House;
import jakarta.persistence.Column;
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
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @Column(columnDefinition = "char(1) default 'M'")
    private String gender;

    @ManyToOne
    @JoinColumn(name = "HOUSE_ID")
    private House house;
}
