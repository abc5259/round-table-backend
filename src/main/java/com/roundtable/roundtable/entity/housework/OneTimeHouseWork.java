package com.roundtable.roundtable.entity.housework;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneTimeHouseWork extends HouseWork {
    @Column
    private LocalDateTime assignedDate;

    public OneTimeHouseWork(String name, HouseWorkCategory houseWorkCategory, Integer currSequence, Integer sequenceSize, LocalDateTime assignedDate) {
        super(name, houseWorkCategory, currSequence, sequenceSize);
        this.assignedDate = assignedDate;
    }
}
