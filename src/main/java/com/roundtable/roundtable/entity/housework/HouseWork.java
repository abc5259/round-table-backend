package com.roundtable.roundtable.entity.housework;

import com.roundtable.roundtable.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseWork extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    protected String name;

    @Column(name = "CATEGORY",nullable = false)
    @Enumerated(EnumType.STRING)
    private HouseWorkCategory houseWorkCategory;

    @Column
    protected Integer currSequence;

    @Column
    protected Integer sequenceSize;

    public HouseWork(String name, HouseWorkCategory houseWorkCategory, Integer currSequence, Integer sequenceSize) {
        this.name = name;
        this.houseWorkCategory = houseWorkCategory;
        this.currSequence = currSequence;
        this.sequenceSize = sequenceSize;
    }

    @OneToMany(mappedBy = "houseWork")
    private List<HouseWorkMember> houseWorkMembers = new ArrayList<>();

    public Long getId() {
        return id;
    }
}
