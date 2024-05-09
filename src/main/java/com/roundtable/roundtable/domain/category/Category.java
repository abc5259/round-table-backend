package com.roundtable.roundtable.domain.category;

import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.house.House;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private String name;

    @NotNull
    private Integer point;

    @NotNull
    private String imageUrl;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private House house;

    @Builder
    private Category(String name, Integer point, House house, String imageUrl) {
        this.name = name;
        this.point = point;
        this.house = house;
        this.imageUrl = imageUrl;
    }

    public static Category create(String name, Integer point, House house, String imageUrl) {
        return Category.builder()
                .name(name)
                .point(point)
                .house(house)
                .imageUrl(imageUrl)
                .build();
    }

    public boolean isSameHouse(House house) {
        return this.house.isEqualId(house);
    }
}
