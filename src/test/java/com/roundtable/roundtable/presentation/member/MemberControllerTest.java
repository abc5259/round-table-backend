package com.roundtable.roundtable.presentation.member;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.roundtable.roundtable.ControllerTestSupport;
import com.roundtable.roundtable.business.member.dto.response.HouseDetailResponse;
import com.roundtable.roundtable.business.member.dto.response.MemberDetailResponse;
import com.roundtable.roundtable.domain.member.Gender;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.response.ApiResponse;
import com.roundtable.roundtable.security.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class MemberControllerTest extends ControllerTestSupport {

    private static final String API_PREFIX = "/members";
    @DisplayName("내 정보를 알 수 있다.")
    @WithMockCustomUser
    @Test
    void me() throws Exception {
        //given

        MemberDetailResponse response = new MemberDetailResponse(
                1L,
                "name",
                Gender.MEN,
                new HouseDetailResponse(
                        1L,
                        "name"
                )
        );
        when(memberService.findMemberDetail(anyLong())).thenReturn(response);

        ApiResponse<Object> expectedResult = ApiResponse.builder()
                .success(true)
                .data(response)
                .build();

        //when
        mockMvc.perform(
                        get(API_PREFIX + "/me")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
    }

    @DisplayName("NotFoundEntityException 에러가 던져지더라도 응답이 간다.")
    @WithMockCustomUser
    @Test
    void me_fail() throws Exception {
        //given
        when(memberService.findMemberDetail(anyLong())).thenThrow(new NotFoundEntityException());

        //when
        mockMvc.perform(
                        get(API_PREFIX + "/me")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.code").isString());
    }
}