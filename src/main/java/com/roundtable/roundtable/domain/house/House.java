package com.roundtable.roundtable.domain.house;

import com.roundtable.roundtable.domain.common.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private String name;

    @NotNull
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

    public static House Id(Long id) {
        return House.builder()
                .id(id)
                .build();
    }
}
