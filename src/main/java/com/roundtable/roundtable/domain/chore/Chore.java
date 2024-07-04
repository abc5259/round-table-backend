package com.roundtable.roundtable.domain.chore;

import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.schedule.Schedule;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Chore extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Schedule schedule;

    @NotNull
    private boolean isCompleted;

    @NotNull
    private LocalDate startDate;

    private String completedImageUrl;

    private String matchKey;

    @OneToMany(mappedBy = "chore", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<ChoreMember> choreMembers = new ArrayList<>();

    @Builder
    private Chore(Schedule schedule, LocalDate startDate, boolean isCompleted, String matchKey, String completedImageUrl) {
        this.schedule = schedule;
        this.startDate = startDate;
        this.isCompleted = isCompleted;
        this.matchKey = matchKey;
        this.completedImageUrl = completedImageUrl;
    }

    public static Chore create(Schedule schedule, LocalDate startDate) {

        return Chore.builder()
                .schedule(schedule)
                .startDate(startDate)
                .build();
    }

    public static Chore create(Schedule schedule, List<ChoreMember> choreMembers, LocalDate startDate) {

        Chore chore = Chore.builder()
                .schedule(schedule)
                .startDate(startDate)
                .build();

        chore.choreMembers = choreMembers;

        return chore;
    }

    public void addChoreMembers(List<ChoreMember> choreMembers) {
        if(this.choreMembers == null) {
            this.choreMembers = new ArrayList<>();
        }

        for (ChoreMember choreMember : choreMembers) {
            if(this.choreMembers != null && !this.choreMembers.contains(choreMember)) {
                this.choreMembers.add(choreMember);
            }
        }
    }

    public void setMatchKey(String matchKey) {
        this.matchKey = matchKey;
    }
}
