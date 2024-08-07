package com.roundtable.roundtable.domain.delegation;

import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delegation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String content;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Chore chore;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member sender;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiver;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DelegationStatus delegationStatus;
}
