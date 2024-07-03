package com.roundtable.roundtable.domain.token;

import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    @Builder
    private Token(Member member, String refreshToken) {
        this.member = member;
        this.refreshToken = refreshToken;
    }

    public static Token of(Long memberId, String refreshToken) {
        return Token.builder()
                .member(Member.Id(memberId))
                .refreshToken(refreshToken)
                .build();
    }

    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
