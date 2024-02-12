package com.roundtable.roundtable.entity.schedule;

import com.roundtable.roundtable.entity.BaseEntity;
import com.roundtable.roundtable.entity.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class ScheduleMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Schedule schedule;

    private ScheduleMember(Member member, Integer sequence) {
        this.member = member;
        this.sequence = sequence;
    }

    public static ScheduleMember of(Member member, Integer sequence) {
        return new ScheduleMember(member, sequence);
    }
}
