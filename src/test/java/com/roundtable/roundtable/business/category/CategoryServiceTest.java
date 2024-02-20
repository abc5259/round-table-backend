package com.roundtable.roundtable.business.category;

import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.category.CategoryRepository;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.HouseRepository;
import com.roundtable.roundtable.implement.category.CreateCategory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("카테고리를 생성한다.")
    @Test
    void createCategory() {
        //given
        House house = House.builder().name("house").build();
        houseRepository.save(house);

        CreateCategory createCategory = new CreateCategory(
                "name",
                10,
                house
        );

        //when
        Long categoryId = categoryService.createCategory(createCategory);

        //then
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        Assertions.assertThat(category).isNotNull()
                .extracting("name", "point", "house")
                .contains(
                        createCategory.name(),
                        createCategory.point(),
                        createCategory.house()
                );
    }
}