package com.roundtable.roundtable.implement.category;

import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.category.CategoryRepository;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.implement.category.CategoryException.CategoryNotFoundException;
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
                .orElseThrow(CategoryNotFoundException::new);
    }

}