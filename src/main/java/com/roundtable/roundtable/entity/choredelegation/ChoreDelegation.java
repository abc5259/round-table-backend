package com.roundtable.roundtable.entity.choredelegation;

import com.roundtable.roundtable.entity.chore.Chore;
import com.roundtable.roundtable.entity.common.BaseEntity;
import com.roundtable.roundtable.entity.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChoreDelegation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Chore chore;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member delegatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member delegatedTo;

    @Enumerated(EnumType.STRING)
    private DelegationStatus delegationStatus;
}
