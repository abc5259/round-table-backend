package com.roundtable.roundtable.domain.member;

import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.global.exception.MemberException.MemberAlreadyHasHouseException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
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

    private String profileUrl;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    private House house;

    @Builder
    private Member(Long id, String email, String password, String name, House house, Gender gender, String profileUrl) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.house = house;
        this.gender = gender;
        this.profileUrl = profileUrl;
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

    public static Member toAuthMember(Long memberId, Long houseId) {
        return Member.builder()
                .id(memberId)
                .house(House.Id(houseId))
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
        return this.house.getId().equals(house.getId());
    }

    public boolean isEnterHouse() {
        return Objects.nonNull(house);
    }
}
