package com.roundtable.roundtable.presentation.category;

import com.roundtable.roundtable.business.category.CategoryService;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.global.response.ApiResponse;
import com.roundtable.roundtable.presentation.category.request.CreateCategoryRequest;
import com.roundtable.roundtable.presentation.support.argumentresolver.Login;
import com.roundtable.roundtable.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createCategory(
            @Login AuthMember authMember,
            @Valid @RequestBody CreateCategoryRequest createCategoryRequest
            ) {
        Long categoryId = categoryService.createCategory(
                createCategoryRequest.toCreateCategory(
                        authMember.memberId(),
                        authMember.houseId()
                )
        );
        return ResponseEntity.ok(SuccessResponse.from(categoryId));
    }

}
