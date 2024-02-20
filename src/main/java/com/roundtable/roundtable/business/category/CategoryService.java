package com.roundtable.roundtable.business.category;

import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.implement.category.CategoryAppender;
import com.roundtable.roundtable.implement.category.CreateCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryAppender categoryAppender;

    public Long createCategory(CreateCategory createCategory) {
        Category category = categoryAppender.appendCategory(createCategory);
        return category.getId();
    }
}
