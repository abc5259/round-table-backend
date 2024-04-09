package com.roundtable.roundtable.business.category;

import static com.roundtable.roundtable.global.exception.errorcode.CategoryErrorCode.NOT_FOUND;

import com.roundtable.roundtable.domain.category.Category;
import com.roundtable.roundtable.domain.category.CategoryRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryReader {

    private final CategoryRepository categoryRepository;

    public Category findCategory(Long categoryId, House house) {
        return categoryRepository.findByIdAndHouse(categoryId, house)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND));
    }

}
