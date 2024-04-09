package com.roundtable.roundtable.domain.house;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InviteCode {

    @Column(nullable = false, unique = true)
    private String code;

    @Builder
    private InviteCode(String code) {
        this.code = code;
    }

    public static InviteCode of(String code) {
        return InviteCode.builder()
                .code(code)
                .build();
    }
}
