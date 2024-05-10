package com.roundtable.roundtable.business.category;

import com.roundtable.roundtable.business.category.dto.CreateCategory;
import com.roundtable.roundtable.business.image.ImageUploader;
import com.roundtable.roundtable.domain.category.Category;
import com.roundtable.roundtable.domain.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryAppender categoryAppender;

    public Long createCategory(MultipartFile image, CreateCategory createCategory) {
        Category category = categoryAppender.appendCategory(
                image,
                createCategory
        );
        return category.getId();
    }
}
