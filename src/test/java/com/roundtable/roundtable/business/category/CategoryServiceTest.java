package com.roundtable.roundtable.business.category;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.category.dto.CreateCategory;
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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
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
        MockMultipartFile mockImageFile = createMockImageFile();
        Mockito.when(imageUploader.upload(mockImageFile)).thenReturn("image.jpg");

        //when
        Long categoryId = categoryService.createCategory(mockImageFile, createCategory);
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
                .imageUrl("a")
                .build();
        categoryRepository.save(category);

        CreateCategory createCategory = new CreateCategory(
                duplicatedCategoryName,
                10,
                member.getId(),
                house.getId()
        );

        MockMultipartFile mockImageFile = createMockImageFile();
        Mockito.when(imageUploader.upload(mockImageFile)).thenReturn("image.jpg");

        //when //then
        assertThatThrownBy(() -> categoryService.createCategory(mockImageFile, createCategory))
                .isInstanceOf(CoreException.DuplicatedException.class)
                .hasMessage(CategoryErrorCode.DUPLICATED_NAME.getMessage());
    }

    private House createHouse() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        return houseRepository.save(house);
    }

    private Member createMemberInHouse(House house) {

        Member member = Member.builder().email("email").password("password").house(house).build();
        return memberRepository.save(member);
    }

    private MockMultipartFile createMockImageFile() {
        // 예제를 위한 임시 이미지 데이터 생성 (실제 이미지 데이터는 다릅니다)
        byte[] imageData = new byte[100]; // 100 바이트 크기의 더미 데이터
        for (int i = 0; i < imageData.length; i++) {
            imageData[i] = (byte)(Math.random() * 256); // 임의의 데이터로 채움
        }
        return  new MockMultipartFile(
                    "image",          // 폼의 input 필드 이름
                    "example.png",          // 업로드 파일 이름
                    "image/png",            // 파일 콘텐츠 타입
                    imageData);
    }
}