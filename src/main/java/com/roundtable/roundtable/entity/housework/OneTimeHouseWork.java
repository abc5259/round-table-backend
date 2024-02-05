package com.roundtable.roundtable.entity.housework;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class OneTimeHouseWork extends HouseWork {
    @Column
    private LocalDateTime assignedDate;
}
