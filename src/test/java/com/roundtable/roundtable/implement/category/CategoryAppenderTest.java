package com.roundtable.roundtable.implement.category;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.category.CategoryRepository;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.HouseRepository;
import com.roundtable.roundtable.global.exception.CategoryException.CategoryDuplicatedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CategoryAppenderTest {

    @Autowired
    private CategoryAppender categoryAppender;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("하우스내에 카테고리를 생성한다.")
    @Test
    void appendCategory() {
        //given
        House house = House.builder().name("house").build();
        houseRepository.save(house);

        CreateCategory createCategory = new CreateCategory(
                "name",
                10,
                house
        );

        //when
        Category category = categoryAppender.appendCategory(createCategory);

        //then
        assertThat(category).isNotNull()
                .extracting("name", "point", "house")
                .contains(
                        createCategory.name(),
                        createCategory.point(),
                        createCategory.house()
                );
     }

     @DisplayName("같은 하우스내에 중복된 이름의 카테고리를 가질 수 없다.")
     @Test
     void appendCategoryWhenDuplicatedCategoryName() {
         //given
         House house = House.builder().name("house").build();
         houseRepository.save(house);

         String duplicatedCategoryName = "name";
         Category category = Category.builder()
                 .name(duplicatedCategoryName)
                 .point(10)
                 .house(house)
                 .build();
         categoryRepository.save(category);

         CreateCategory createCategory = new CreateCategory(
                 duplicatedCategoryName,
                 10,
                 house
         );


         //when //then
         assertThatThrownBy(() -> categoryAppender.appendCategory(createCategory))
                 .isInstanceOf(CategoryDuplicatedException.class)
                 .hasMessage(duplicatedCategoryName + "에 해당하는 cateogy가 이미 존재합니다.");
      }
}