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

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class HouseWork extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer currSequence;

    @Column
    private Integer sequenceSize;

    @Column(name = "CATEGORY",nullable = false)
    @Enumerated(EnumType.STRING)
    private HouseWorkCategory houseWorkCategory;

    @OneToMany(mappedBy = "houseWork")
    private List<HouseWorkMember> houseWorkMembers = new ArrayList<>();
}
