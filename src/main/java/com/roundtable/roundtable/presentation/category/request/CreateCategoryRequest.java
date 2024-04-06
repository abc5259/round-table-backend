package com.roundtable.roundtable.presentation.category.request;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.business.category.CreateCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record CreateCategoryRequest(
        @NotBlank(message = "이름은 필수입니다.")
        String name,
        @NotEmpty(message = "포인트는 필수입니다.")
        @Positive(message = "포인트는 0보다 커야합니다.")
        Integer point
) {


    public CreateCategory toCreateCategory(Long memberId, Long houseId) {
        return new CreateCategory(
                name,
                point,
                memberId,
                houseId
        );
    }
}
