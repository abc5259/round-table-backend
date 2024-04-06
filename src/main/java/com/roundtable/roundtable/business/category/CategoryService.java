package com.roundtable.roundtable.business.category;

import com.roundtable.roundtable.entity.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryAppender categoryAppender;

    public Long createCategory(CreateCategory createCategory) {
        Category category = categoryAppender.appendCategory(createCategory);
        return category.getId();
    }
}
