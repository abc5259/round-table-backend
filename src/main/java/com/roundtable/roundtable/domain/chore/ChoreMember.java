package com.roundtable.roundtable.domain.chore;

import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Chore chore;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    private ChoreMember(Chore chore, Member member) {
        this.chore = chore;
        this.member = member;
    }

    public ChoreMember setBulkInsert(Chore chore) {
        return ChoreMember.builder()
                .chore(chore)
                .member(member)
                .build();
    }

    public static ChoreMember create(Chore chore, Member member) {
        return ChoreMember.builder()
                .chore(chore)
                .member(member)
                .build();
    }
}
