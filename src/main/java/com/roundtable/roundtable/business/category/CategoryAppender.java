package com.roundtable.roundtable.business.category;

import static com.roundtable.roundtable.global.exception.CoreException.*;

import com.roundtable.roundtable.business.house.HouseValidator;
import com.roundtable.roundtable.business.member.MemberValidator;
import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.category.CategoryRepository;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.global.exception.CoreException;
import com.roundtable.roundtable.global.exception.errorcode.CategoryErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class CategoryAppender {

    private final CategoryRepository categoryRepository;

    private final MemberValidator memberValidator;

    public Category appendCategory(CreateCategory createCategory) {
        validate(createCategory);

        Category category = Category.create(
                createCategory.name(),
                createCategory.point(),
                House.Id(createCategory.houseId()));

        return categoryRepository.save(category);
    }

    private void validate(CreateCategory createCategory) {
        memberValidator.validateMemberBelongsToHouse(createCategory.memberId(), createCategory.houseId());
        checkDuplicatedCategoryName(createCategory);
    }

    private void checkDuplicatedCategoryName(CreateCategory createCategory) {
        boolean isExistCategory = categoryRepository.existsByNameAndHouse(createCategory.name(), House.Id(createCategory.houseId()));
        if(isExistCategory) {
            throw new DuplicatedException(CategoryErrorCode.DUPLICATED_NAME);
        }
    }
}
