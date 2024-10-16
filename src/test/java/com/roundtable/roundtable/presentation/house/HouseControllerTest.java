package com.roundtable.roundtable.presentation.house;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.roundtable.roundtable.ControllerTestSupport;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.house.dto.HouseMember;
import com.roundtable.roundtable.presentation.house.request.CreateHouseRequest;
import com.roundtable.roundtable.presentation.house.response.HouseMemberResponse;
import com.roundtable.roundtable.security.WithMockCustomUser;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class HouseControllerTest extends ControllerTestSupport {

    private final static String API_PREFIX = "/houses";

    @DisplayName("새로운 하우스를 생성한다.")
    @WithMockCustomUser
    @Test
    void createHouse() throws Exception {
        //given
        CreateHouseRequest request = new CreateHouseRequest("house", new ArrayList<>());

        when(houseService.createHouse(request.toCreateHouse(), new AuthMember(anyLong(), eq(null)))).thenReturn(1L);

        //when
        mockMvc.perform(
                        post(API_PREFIX)
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data.houseId").isNumber())
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.code").isEmpty());

    }

    @DisplayName("새로운 하우스를 생성할때 이름이 있어야한다.")
    @WithMockCustomUser
    @Test
    void createHouse_non_name() throws Exception {
        //given
        CreateHouseRequest request = new CreateHouseRequest(null, new ArrayList<>());

        when(houseService.createHouse(request.toCreateHouse(), new AuthMember(anyLong(), anyLong()))).thenReturn(1L);

        //when
        mockMvc.perform(
                        post(API_PREFIX)
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("하우스 이름은 필수입니다."))
                .andExpect(jsonPath("$.code").isEmpty());

    }

    @DisplayName("하우스에 속한 맴버를 조회한다.")
    @WithMockCustomUser
    @Test
    void findHouseMembers() throws Exception {
        //given
        List<HouseMember> houseMembers = List.of(
                new HouseMember(1L, "name", "url")
        );
        when(houseService.findHouseMembers(any())).thenReturn(houseMembers);

        //when
        ResultActions resultActions = mockMvc.perform(
                        get(API_PREFIX + "/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                );

        //then

        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.code").isEmpty());

    }
}