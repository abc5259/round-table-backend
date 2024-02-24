package com.roundtable.roundtable.entity.category;

import com.roundtable.roundtable.entity.BaseEntity;
import com.roundtable.roundtable.entity.house.House;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer point;

    @ManyToOne(fetch = FetchType.LAZY)
    private House house;

    @Builder
    private Category(String name, Integer point, House house) {
        this.name = name;
        this.point = point;
        this.house = house;
    }

    public static Category create(String name, Integer point, House house) {
        return Category.builder()
                .name(name)
                .point(point)
                .house(house)
                .build();
    }

    public boolean isSameHouse(House house) {
        return this.house.isEqualId(house);
    }
}
