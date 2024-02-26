package com.roundtable.roundtable.entity.chore;

import com.roundtable.roundtable.entity.common.BaseEntity;
import com.roundtable.roundtable.entity.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChoreMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Chore chore;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    private ChoreMember(Chore chore, Member member) {
        this.chore = chore;
        this.member = member;
    }

    public static ChoreMember create(Chore chore, Member member) {
        return ChoreMember.builder()
                .chore(chore)
                .member(member)
                .build();
    }
}
