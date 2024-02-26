package com.roundtable.roundtable.entity.house;

import com.roundtable.roundtable.entity.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class House extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public static final int MAX_MEMBER_SIZE = 50;

    @Builder
    private House(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static House of(String name) {
        return House.builder()
                .name(name)
                .build();
    }

    public Long getId() {
        return id;
    }

    public boolean isEqualId(House house) {
        return this.id.equals(house.getId());
    }
}
