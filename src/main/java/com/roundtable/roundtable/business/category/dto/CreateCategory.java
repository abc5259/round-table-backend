package com.roundtable.roundtable.business.category.dto;

public record CreateCategory(

        String name,
        Integer point,
        Long memberId,
        Long houseId,
        String imageUrl
) {
    public CreateCategory(String name,Integer point, Long memberId, Long houseId) {
        this(name, point, memberId, houseId, null);
    }

    public CreateCategory(CreateCategory createCategory, String imageUrl) {
        this(createCategory.name, createCategory.point, createCategory.memberId, createCategory.houseId, imageUrl);
    }
}
