package com.roundtable.roundtable.business.category;

import static com.roundtable.roundtable.global.exception.CoreException.*;

import com.roundtable.roundtable.business.category.dto.CreateCategory;
import com.roundtable.roundtable.business.image.ImageUploader;
import com.roundtable.roundtable.domain.category.Category;
import com.roundtable.roundtable.domain.category.CategoryRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.global.exception.errorcode.CategoryErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
@Transactional
@RequiredArgsConstructor
public class CategoryAppender {

    private final ImageUploader imageUploader;

    private final CategoryRepository categoryRepository;

    public Category appendCategory(MultipartFile imageFile, CreateCategory createCategory) {
        checkDuplicatedCategoryName(createCategory);

        String url = imageUploader.upload(imageFile);

        Category category = Category.create(
                createCategory.name(),
                createCategory.point(),
                House.Id(createCategory.houseId()),
                url);

        return categoryRepository.save(category);
    }

    private void checkDuplicatedCategoryName(CreateCategory createCategory) {
        boolean isExistCategory = categoryRepository.existsByNameAndHouse(createCategory.name(), House.Id(createCategory.houseId()));
        if(isExistCategory) {
            throw new DuplicatedException(CategoryErrorCode.DUPLICATED_NAME);
        }
    }
}
