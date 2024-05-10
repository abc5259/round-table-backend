package com.roundtable.roundtable.presentation.category;

import com.roundtable.roundtable.business.category.CategoryService;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.global.response.ApiResponse;
import com.roundtable.roundtable.presentation.category.request.CreateCategoryRequest;
import com.roundtable.roundtable.global.support.annotation.Login;
import com.roundtable.roundtable.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping(value = "/house/{houseId}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse<Long>> createCategory(
            @Login AuthMember authMember,
            @PathVariable Long houseId,
            @RequestPart MultipartFile image,
            @Valid @RequestPart CreateCategoryRequest createCategoryRequest
    ) {
        Long categoryId = categoryService.createCategory(
                image,
                createCategoryRequest.toCreateCategory(
                        authMember.memberId(),
                        houseId
                )
        );
        return ResponseEntity.ok(SuccessResponse.from(categoryId));
    }

}
