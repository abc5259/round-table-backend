package com.roundtable.roundtable.entity.member;

import com.roundtable.roundtable.entity.common.BaseEntity;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.global.exception.MemberException.MemberAlreadyHasHouseException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private House house;

    @Builder
    private Member(Long id, String email, String password, String name, House house) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.house = house;
    }

    public static Member Id(Long id) {
        return Member.builder()
                .id(id)
                .build();
    }

    public static Member of(String email, String password, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
    }

    public boolean isCorrectPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }

    public void settingProfile(String name, Gender gender) {
        this.name = name;
        this.gender = gender;
    }

    public void enterHouse(House house) {
        if(this.house != null) {
            throw new MemberAlreadyHasHouseException();
        }
        this.house = house;
    }

    public boolean isSameHouse(House house) {
        return this.house.isEqualId(house);
    }
}
