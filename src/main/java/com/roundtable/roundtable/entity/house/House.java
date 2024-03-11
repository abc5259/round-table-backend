package com.roundtable.roundtable.entity.house;

import com.roundtable.roundtable.entity.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class House extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private InviteCode inviteCode;

    @Builder
    private House(Long id, String name, InviteCode inviteCode) {
        this.id = id;
        this.name = name;
        this.inviteCode = inviteCode;
    }

    public static House of(String name, InviteCode inviteCode) {
        return House.builder()
                .name(name)
                .inviteCode(inviteCode)
                .build();
    }

    public boolean isEqualId(House house) {
        return getId().equals(house.getId());
    }
}
