package com.roundtable.roundtable.entity.category;

import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.HouseRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private HouseRepository houseRepository;

    @DisplayName("하우스내에 같은 이름을 가진 카테고리가 있는지 확인한다.")
    @CsvSource({
            "category1, category1, true",
            "category1, category2, false",
    })
    @ParameterizedTest
    void existsByNameAndHouse(String name1, String name2, boolean result) {
        //given
        House house = House.builder().name("house").build();
        houseRepository.save(house);

        Category category1 = Category.builder()
                .name(name1)
                .point(20)
                .house(house)
                .build();
        categoryRepository.save(category1);

        //when
        boolean isExist = categoryRepository.existsByNameAndHouse(name2, house);

        //then
        Assertions.assertThat(isExist).isEqualTo(result);
     }

    @DisplayName("하우스가 서로 다르면 카테고리 이름이 같을 수 있다.")
    @Test
    void existsByNameAndHouseWhenDifferentHouse() {
        //given
        House house1 = House.builder().name("house1").build();
        houseRepository.save(house1);

        House house2 = House.builder().name("house2").build();
        houseRepository.save(house2);

        String name = "name";
        Category category1 = Category.builder()
                .name(name)
                .point(20)
                .house(house1)
                .build();
        categoryRepository.save(category1);

        //when
        boolean result = categoryRepository.existsByNameAndHouse(name, house2);

        //then
        Assertions.assertThat(result).isFalse();
    }
}