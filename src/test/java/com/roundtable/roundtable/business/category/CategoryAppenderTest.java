package com.roundtable.roundtable.business.category;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.category.CategoryRepository;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.HouseRepository;
import com.roundtable.roundtable.entity.house.InviteCode;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.member.MemberRepository;
import com.roundtable.roundtable.global.exception.CoreException;
import com.roundtable.roundtable.global.exception.MemberException.MemberNoHouseException;
import com.roundtable.roundtable.global.exception.MemberException.MemberNotSameHouseException;
import com.roundtable.roundtable.global.exception.errorcode.CategoryErrorCode;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class CategoryAppenderTest extends IntegrationTestSupport {

    @Autowired
    private CategoryAppender categoryAppender;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("하우스내에 카테고리를 생성한다.")
    @Test
    void appendCategory() {
        //given
        House house = createHouse();
        Member member = createMemberInHouse(house);

        CreateCategory createCategory = new CreateCategory(
                "name",
                10,
                member.getId(),
                house.getId()
        );

        //when
        Category category = categoryAppender.appendCategory(createCategory);

        //then
        assertThat(category).isNotNull()
                .extracting("name", "point")
                .contains(
                        createCategory.name(),
                        createCategory.point()
                );

        assertThat(category.getHouse().getId()).isEqualTo(house.getId());
     }

     @DisplayName("같은 하우스내에 중복된 이름의 카테고리를 가질 수 없다.")
     @Test
     void appendCategoryWhenDuplicatedCategoryName() {
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
         assertThatThrownBy(() -> categoryAppender.appendCategory(createCategory))
                 .isInstanceOf(CoreException.DuplicatedException.class)
                 .hasMessage(CategoryErrorCode.DUPLICATED_NAME.getMessage());
      }

    @DisplayName("하우스에 속하지 않은 멤버라면 category를 추가할 수 없다.")
      @Test
      void appendCategoryWhenNoHasHouseMember() {
          //given
          Member member = createMemberInHouse(null);

          CreateCategory createCategory = new CreateCategory(
                  "category",
                  10,
                  member.getId(),
                  null
          );

          //when //then
          assertThatThrownBy(() -> categoryAppender.appendCategory(createCategory))
                  .isInstanceOf(MemberNoHouseException.class)
                  .hasMessage(MemberErrorCode.NO_HAS_HOUSE.getMessage());
       }

       @DisplayName("해당 하우스에 참여하지 않은 Member라면 에러를 던진다.")
       @Test
       void appendCategoryWhenNotMathHouseAndMember() {
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
           assertThatThrownBy(() -> categoryAppender.appendCategory(createCategory))
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