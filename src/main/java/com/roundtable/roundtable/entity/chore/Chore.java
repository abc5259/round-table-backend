package com.roundtable.roundtable.entity.chore;

import com.roundtable.roundtable.entity.BaseEntity;
import com.roundtable.roundtable.entity.schedule.Schedule;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chore extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Schedule schedule;

    @Column(nullable = false)
    private boolean isCompleted;

    @OneToMany(mappedBy = "chore", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ChoreMember> choreMembers = new ArrayList<>();

    @Builder
    private Chore(Schedule schedule) {
        this.schedule = schedule;
    }

    public static Chore create(Schedule schedule) {

        return Chore.builder()
                .schedule(schedule)
                .build();
    }

    public void addChoreMembers(List<ChoreMember> choreMembers) {
        for (ChoreMember choreMember : choreMembers) {
            if(this.choreMembers != null && !this.choreMembers.contains(choreMember)) {
                this.choreMembers.add(choreMember);
            }
        }
    }
}
