package com.roundtable.roundtable.business.category;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.category.Category;
import com.roundtable.roundtable.domain.category.CategoryRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.global.exception.CoreException;
import com.roundtable.roundtable.global.exception.MemberException.MemberNoHouseException;
import com.roundtable.roundtable.global.exception.MemberException.MemberNotSameHouseException;
import com.roundtable.roundtable.global.exception.errorcode.CategoryErrorCode;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class CategoryServiceTest extends IntegrationTestSupport {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("카테고리를 생성한다.")
    @Test
    void createCategory() {
        //given
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        house = houseRepository.save(house);
        Member member = createMemberInHouse(house);

        CreateCategory createCategory = new CreateCategory(
                "name",
                10,
                member.getId(),
                house.getId()
        );

        //when
        Long categoryId = categoryService.createCategory(createCategory);
        entityManager.flush();
        entityManager.clear();

        //then
        Category category = categoryRepository.findById(categoryId).orElseThrow();

        assertThat(category).isNotNull()
                .extracting("name", "point")
                .contains(
                        createCategory.name(),
                        createCategory.point()
                );

        assertThat(category.getHouse()).isNotNull()
                .extracting("id")
                .isEqualTo(house.getId());
    }

    @DisplayName("같은 하우스내에 중복된 이름의 카테고리를 가질 수 없다.")
    @Test
    void createCategoryWhenDuplicatedCategoryName() {
        //given
        House house = createHouse();
        Member member = createMemberInHouse(house);

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
                member.getId(),
                house.getId()
        );


        //when //then
        assertThatThrownBy(() -> categoryService.createCategory(createCategory))
                .isInstanceOf(CoreException.DuplicatedException.class)
                .hasMessage(CategoryErrorCode.DUPLICATED_NAME.getMessage());
    }

    @DisplayName("하우스에 속하지 않은 멤버라면 category를 추가할 수 없다.")
    @Test
    void createCategoryWhenNoHasHouseMember() {
        //given
        Member member = createMemberInHouse(null);

        CreateCategory createCategory = new CreateCategory(
                "category",
                10,
                member.getId(),
                null
        );

        //when //then
        assertThatThrownBy(() -> categoryService.createCategory(createCategory))
                .isInstanceOf(MemberNoHouseException.class)
                .hasMessage(MemberErrorCode.NO_HAS_HOUSE.getMessage());
    }

    @DisplayName("해당 하우스에 참여하지 않은 Member라면 에러를 던진다.")
    @Test
    void createCategoryWhenNotMathHouseAndMember() {
        //given
        House house = createHouse();
        Member member = createMemberInHouse(house);

        CreateCategory createCategory = new CreateCategory(
                "category",
                10,
                member.getId(),
                house.getId() + 1
        );

        //when //then
        assertThatThrownBy(() -> categoryService.createCategory(createCategory))
                .isInstanceOf(MemberNotSameHouseException.class)
                .hasMessage(MemberErrorCode.NOT_SAME_HOUSE.getMessage());

    }

    private House createHouse() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        return houseRepository.save(house);
    }

    private Member createMemberInHouse(House house) {

        Member member = Member.builder().email("email").password("password").house(house).build();
        return memberRepository.save(member);
    }
}